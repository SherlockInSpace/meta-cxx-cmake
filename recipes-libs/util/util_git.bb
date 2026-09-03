# util: the cxx-cmake C++ library template, pinned by SRCREV to a commit on
# its "library" branch.
#
# BASELINE RECIPE. This is the first BitBake build of the library and it is
# expected to FAIL packaging QA against the pinned commit:
#
#   * the library is installed as an unversioned libutil.so (its SONAME is
#     the bare "libutil.so"). The default FILES:${PN}-dev claims
#     ${libdir}/lib*.so, so the real ELF object lands in util-dev and
#     do_package_qa reports the dev-elf error, while the runtime package
#     util is left without the library;
#   * utilConfig.cmake, utilConfigVersion.cmake and utilTargets.cmake are
#     installed under ${datadir}/util/cmake, which no -dev FILES pattern
#     matches, so they are packaged into the runtime package util
#     (visible with `oe-pkgdata-util list-pkg-files util`).
#
# Those failures are the acceptance evidence for the library's install-layout
# fixes (versioned SONAME, CMake package config under ${libdir}/cmake): once
# they land, bumping SRCREV must make this recipe pass QA unchanged. Do not
# add INSANE_SKIP, FILES tweaks or anything else that hides them here.

SUMMARY = "cxx-cmake C++ library template"
DESCRIPTION = "Example C++ shared library from the cxx-cmake template family, \
built with CMake, linked against OpenSSL and installed with a CMake package \
configuration for downstream find_package(util)."
HOMEPAGE = "https://github.com/SherlockInSpace/cxx-cmake"
LICENSE = "MIT"
# The repository's own LICENSE file, so a licence change breaks the recipe by
# design. Recompute with `md5sum LICENSE` at the SRCREV commit when bumping.
LIC_FILES_CHKSUM = "file://LICENSE;md5=940a18db9c718275476294053bc4dbb9"

PV = "0.1.0+git"
SRC_URI = "git://github.com/SherlockInSpace/cxx-cmake.git;protocol=https;branch=library"
# Tip of the library branch at the time of writing. Bump deliberately.
SRCREV = "e362a7df2b109676672787b02a11eb3b3878c40f"
# No S: wrynose unpacks the git tree into ${UNPACKDIR}/${BP}, the default S.

DEPENDS = "openssl"

inherit cmake pkgconfig

# Option names of the pinned tree; BUILD_TESTING/BUILD_DOCS arrive with the
# library's CMake rework and will be switched here when SRCREV moves past it.
EXTRA_OECMAKE = "-DBUILD_TEST=OFF -DBUILD_DOC=OFF"
