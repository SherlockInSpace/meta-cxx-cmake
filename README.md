# meta-cxx-cmake

Yocto layer for the cxx-cmake template family.

## Purpose

This layer provides:

- recipes for the [cxx-cmake library template](https://github.com/SherlockInSpace/cxx-cmake-library)
  and the cxx-cmake application template, and
- [kas](https://kas.readthedocs.io/) configuration files for building them.

It targets Yocto Project 6.0 (`wrynose`).

## Recipes

- `recipes-libs/util/util_git.bb` builds the library template (`util`) at
  a release tag, now `v0.2.0`. `util` gets the versioned `libutil.so.*`;
  `util-dev` gets the headers under `include/util/`, the `libutil.so`
  link and the CMake package under `lib/cmake/util/`. The recipe adds no
  packaging rules of its own, so a QA failure points at the library.

## Dependencies

This layer depends on:

- URI: `https://git.openembedded.org/openembedded-core`
  - layer: `meta`
  - branch: `wrynose`
- URI: `https://git.openembedded.org/meta-openembedded`
  - layer: `meta-oe`
  - branch: `wrynose`

## Adding the layer to your build

Clone this repository next to your other layers, then, from an initialised
build directory (`source oe-init-build-env`), run:

```sh
bitbake-layers add-layer ../meta-cxx-cmake
```

`bitbake-layers add-layer` checks that the layer's dependencies (`core` and
`openembedded-layer`) are already present in `conf/bblayers.conf`, so add
`openembedded-core/meta` and `meta-openembedded/meta-oe` first.

## Building locally

`kas/qemuarm64.yml` composes Yocto Project 6.0.2 (`wrynose`) from `bitbake`,
`openembedded-core`, `meta-yocto` and `meta-openembedded` plus this layer, now
that the combined poky repository is retired. `kas/qemuarm64.lock.yml`, loaded
automatically, fixes each repo at its `yocto-6.0.2` commit (a `wrynose` commit
for meta-openembedded, which the point releases do not tag). To bump the
commits, run `kas lock --update kas/qemuarm64.yml` or edit the lock file.
`kas/check-layer.yml` is the same build with this layer and `meta-oe` left out of
`bblayers.conf`; `yocto-check-layer` adds them itself, and CI runs it on every pull
request:

```sh
kas-container shell kas/check-layer.yml -c \
    'yocto-check-layer --no-auto-dependency \
       --dependency /work/openembedded-core/meta /work/meta-openembedded/meta-oe -- /repo'
```

`MACHINE` is `qemuarm64` as a build target only. There is no image build and
no SDK yet, and nothing runs under QEMU. Those come later, with the container
repo's `yocto` image.

### One-time setup

`kas-container` runs kas inside `ghcr.io/siemens/kas/kas:5.5`, so the host
needs only docker (or podman) and the script:

```sh
pipx install kas==5.5   # installs kas-container next to kas
```

Put the three directories kas uses outside this repo. `KAS_WORK_DIR` defaults
to the current directory, which would drop four clones and `build/` into this
checkout. The other two are bitbake's shared-state and download caches and
should outlive a deleted work directory, so the next build starts warm.
`kas-container` bind-mounts them as `/work`, `/sstate` and `/downloads` and
passes the paths on to bitbake.

```sh
export KAS_WORK_DIR=~/yocto/work
export SSTATE_DIR=~/yocto/sstate
export DL_DIR=~/yocto/downloads
mkdir -p "$KAS_WORK_DIR" "$SSTATE_DIR" "$DL_DIR"
```

### The loop

From the root of this repo:

```sh
kas-container build --target util kas/qemuarm64.yml
```

A cold build runs 984 tasks and takes about 20 minutes on a 20-core machine.
With warm caches a rebuild of `util` takes under a minute in an existing work
directory, a few minutes in a fresh one. The console log is
`$KAS_WORK_DIR/build/tmp/log/cooker/qemuarm64/console-latest.log`. The
per-task logs for `util`, QA output included, are under
`$KAS_WORK_DIR/build/tmp/work/cortexa57-poky-linux/util/0.2.0/temp/`.

The build should pass packaging QA. To see the split into `util` (the
versioned library) and `util-dev` (headers, the `libutil.so` link and the
CMake package), run `oe-pkgdata-util list-pkg-files util util-dev` from
`kas-container shell kas/qemuarm64.yml`. A QA failure here is a library
bug: fix the install layout there rather than adding `INSANE_SKIP` to the
recipe.

### Iterating on a checkout

`kas/externalsrc.yml` builds `util` from `/work/cxx-cmake-library` instead of
the recipe's `SRCREV`, so bind-mount a checkout there to build a change before
pushing it:

```sh
kas-container \
    --runtime-args "-v /path/to/cxx-cmake-library:/work/cxx-cmake-library" \
    build kas/qemuarm64.yml:kas/externalsrc.yml
```

The checkout has to be writable, since `externalsrc` drops `oe-workdir` and
`oe-logs` symlinks into it, and `LIC_FILES_CHKSUM` is still checked against
its `LICENSE`. `kas-container` overwrites `KAS_RUNTIME_ARGS`, so setting it in
the environment does nothing.

## Maintainer

Ryan Sherlock <ryan.m.sherlock@gmail.com>

## Submitting patches

Patches are submitted as GitHub pull requests against the `main` branch of
<https://github.com/SherlockInSpace/meta-cxx-cmake>. Please follow the
[Conventional Commits](https://www.conventionalcommits.org/) format for
every commit subject. CI lints each commit in the pull request with
commitlint. Pull requests are rebase-merged, one to a few commits each. Bug
reports and feature requests go to the repository's
[issue tracker](https://github.com/SherlockInSpace/meta-cxx-cmake/issues).

## License

This layer is released under the MIT license; see [COPYING.MIT](COPYING.MIT).
