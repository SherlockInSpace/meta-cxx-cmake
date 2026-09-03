# meta-cxx-cmake

Yocto layer for the cxx-cmake template family.

## Purpose

This layer provides:

- recipes for the [cxx-cmake library template](https://github.com/SherlockInSpace/cxx-cmake)
  and the cxx-cmake application template, and
- [kas](https://kas.readthedocs.io/) configuration files for building them.

It targets Yocto Project 6.0 (`wrynose`). This repository currently contains
only the layer skeleton; recipes will follow.

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
