# AGENTS.md

Instructions for coding agents working in this repository. People should read
[CONTRIBUTING.md](CONTRIBUTING.md); this is the short form of the same rules.

## Checks

| Task | Command |
|---|---|
| Layer check | `kas-container shell kas/check-layer.yml -c 'yocto-check-layer --no-auto-dependency --dependency /work/openembedded-core/meta /work/meta-openembedded/meta-oe -- /repo'` |
| SDK dry runs | `kas-container build kas/sdk.yml -- -n`, `kas-container build kas/sdk-aarch64.yml -- -n` |
| Full build (needs a build machine) | `kas-container build --target util --target cxx-cmake-app --target tracy kas/qemuarm64.yml` |

CI runs the first two. Say in the pull request whether you ran the full build.

## Rules

- Read every `SRCREV` and checksum from its source (`git ls-remote`,
  `md5sum`); never type or copy one by hand.
- No `INSANE_SKIP` or extra `FILES` to get past packaging QA. Fix the
  template's install layout instead.
- Nothing runs under QEMU. `qemuarm64` is a build target only.
- Commits are Conventional Commits with the issue in the body (`Closes #N` or
  `Refs #N`); each commit must stand on its own.
