# Contributing

How changes land in this layer and how releases are made.
[README.md](README.md) covers using the layer and building it.

## From issue to merge

1. **Start from an issue.** Its title is a Conventional Commit subject
   (`feat(recipes): ...`, `feat(kas): ...`, `ci: ...`) and its body ends in a
   "Done when" list.
2. **Branch** as `issue/<number>-<short-slug>` from `main`.
3. **Commit** in Conventional Commit style, with `Closes #N` or `Refs #N` in
   the body. Pull requests are rebase-merged, so every commit lands as it was
   reviewed and has to stand on its own.
4. **Keep pull requests small**: one issue, one to a few commits. Say in the
   description whether you ran a BitBake build and which targets.
5. **CI is the gate**: `yocto-check-layer`, the kas checkout against the lock
   file, the dry runs of both SDK builds, and commit lint. GitHub's hosted
   runners are too small to build the layer, so CI parses it and runs no
   BitBake tasks.

## Recipes

- A recipe builds a release tag: `PV` is the version, `tag=v${PV}` is in
  `SRC_URI`, and `SRCREV` is the tag's commit. Read the commit with
  `git ls-remote <repo> 'refs/tags/vX.Y.Z^{}' refs/tags/vX.Y.Z` instead of
  copying it, and recompute `LIC_FILES_CHKSUM` (`md5sum LICENSE` at the tag)
  whenever `SRCREV` moves.
- util and the app move together. The app asks `find_package` for a util
  version, and util's package version file accepts only the same minor
  version.
- Packaging QA has to pass without `INSANE_SKIP` or extra `FILES`. A QA
  failure is a bug in the template's install layout and is fixed there.
- Nothing runs under QEMU. `qemuarm64` is a build target; ptest packages run
  on hardware.

A recipe change is done when these pass packaging QA, the second with the
app's `tracy` option on:

```sh
kas-container build --target util --target cxx-cmake-app --target tracy kas/qemuarm64.yml
kas-container build kas/app-tracy.yml
```

## Releases

A release of this layer carries the SDK installers the container's `yocto`
image downloads. Releases are made by hand from a commit on `main`:

1. Build both SDKs from that commit (README, "The SDK").
2. Write `SHA256SUMS` over the two installers and `kas/qemuarm64.lock.yml`.
3. Create the release `vX.Y.Z` at that commit and attach the installers,
   `SHA256SUMS` and the lock file. A recipe moving is a minor version; a
   rebuild with nothing changed is a patch.
4. In cxx-cmake-container, move `META_CXX_CMAKE_VERSION` and the two
   installer hashes.
