# meta-cxx-cmake

Yocto layer for the cxx-cmake template family.

## Purpose

This layer provides:

- recipes for the [cxx-cmake library template](https://github.com/SherlockInSpace/cxx-cmake)
  and the cxx-cmake application template, and
- [kas](https://kas.readthedocs.io/) configuration files for building them.

It targets Yocto Project 6.0 (`wrynose`).

## Recipes

- `recipes-libs/util/util_git.bb` builds the library template (`util`) from
  a `SRCREV` pinned to a commit on its `library` branch, with
  `LIC_FILES_CHKSUM` taken from the repository's own `LICENSE` file so a
  licence change breaks the recipe by design. It is the baseline recipe: the
  header comment records the packaging-QA failures the pinned commit is
  expected to produce (an unversioned `libutil.so`, CMake config files in
  the runtime package) as the acceptance evidence for the library's
  install-layout fixes. Nothing in the recipe hides them.

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

## Building with kas

The `kas/` directory holds [kas](https://kas.readthedocs.io/) configuration
for building this layer against Yocto Project 6.0.2 (`wrynose`). The combined
`poky` repository is retired, so the build is composed from the four component
repositories (`bitbake`, `openembedded-core`, `meta-yocto`, `meta-openembedded`)
plus this layer:

- `kas/qemuarm64.yml` tracks the `wrynose` branches (`2.18` for bitbake);
- `kas/qemuarm64.lock.yml` pins bitbake, openembedded-core and meta-yocto to
  their `yocto-6.0.2` commits and meta-openembedded (which the Yocto point
  releases do not tag) to a `wrynose` commit; it is loaded automatically;
- `kas/check-layer.yml` is the same build with this layer left out of
  `bblayers.conf`, for `yocto-check-layer`.

Install kas 5.5 either natively or as a container:

```sh
pipx install kas==5.5        # provides both `kas` and `kas-container`
kas checkout kas/qemuarm64.yml            # native
kas-container checkout kas/qemuarm64.yml  # needs docker or podman; runs in
                                          # ghcr.io/siemens/kas/kas:5.5
```

Keep the shared state and download caches *outside* the kas work directory
(and its `build/` subdirectory), so a fresh work directory still starts warm
and every run primes the caches for the next one:

```sh
export SSTATE_DIR=~/yocto/sstate
export DL_DIR=~/yocto/downloads
kas checkout kas/qemuarm64.yml   # clone and pin the repos, write build/conf
kas build kas/qemuarm64.yml      # optional: build core-image-minimal
```

kas passes `SSTATE_DIR` and `DL_DIR` through to bitbake. To bump the pins,
run `kas lock --update kas/qemuarm64.yml` (moves every repository to its
branch head) or edit the commits in `kas/qemuarm64.lock.yml` to match a
specific point release; `KAS_CLONE_DEPTH=1` is enough to check out the pinned
commits, as CI does with `kas-container`.

`MACHINE` is `qemuarm64` as a build target only: nothing is executed under
QEMU.

To build just the library, pass `--target util`:

```sh
kas build kas/qemuarm64.yml --target util
```

### Building util from a local checkout

`kas/externalsrc.yml` is the dev-loop overlay: it inherits `externalsrc` and
points `EXTERNALSRC:pn-util` at `/work/cxx-cmake-library`, so `util` is built
from that checkout instead of the pinned `SRCREV`: `do_fetch`, `do_unpack`
and `do_patch` are skipped, but `do_populate_lic` still runs, so
`LIC_FILES_CHKSUM` is checked against the checkout's `LICENSE` and a local
licence edit fails the build until the recipe's md5 is updated. The overlay
also sets `target: util`. Include it after the base config, and make the
checkout visible at that path inside the build environment. With
`kas-container`, `/work` is the `KAS_WORK_DIR` mount, so either keep the
checkout at `$KAS_WORK_DIR/cxx-cmake-library` or bind-mount it with
`--runtime-args` ("Additional arguments to pass to the container runtime",
`kas-container --help`):

```sh
kas-container \
    --runtime-args "-v /path/to/cxx-cmake-library:/work/cxx-cmake-library" \
    build kas/qemuarm64.yml:kas/externalsrc.yml
```

With native kas, `/work/cxx-cmake-library` is a plain host path. The
checkout must be writable: `externalsrc` drops `oe-workdir` and `oe-logs`
symlinks into it.

## Maintainer

Ryan Sherlock <ryan.m.sherlock@gmail.com>

## Submitting patches

Patches are submitted as GitHub pull requests against the `main` branch of
<https://github.com/SherlockInSpace/meta-cxx-cmake>. Please follow the
[Conventional Commits](https://www.conventionalcommits.org/) format for the
pull request title; pull requests are squash-merged. Bug reports and feature
requests go to the repository's
[issue tracker](https://github.com/SherlockInSpace/meta-cxx-cmake/issues).

## License

This layer is released under the MIT license; see [COPYING.MIT](COPYING.MIT).
