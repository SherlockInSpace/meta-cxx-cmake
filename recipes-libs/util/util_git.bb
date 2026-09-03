# Baseline recipe for the library template. At this SRCREV do_package_qa
# fails. libutil.so has no version, so it lands in util-dev (dev-elf) and
# util is empty. utilConfig*.cmake and utilTargets.cmake install under
# ${datadir}/util/cmake and end up in util too. The fix is in the library
# (versioned SONAME, config under ${libdir}/cmake). Do not hide it here
# with INSANE_SKIP or FILES tweaks.

SUMMARY = "cxx-cmake C++ library template"
DESCRIPTION = "Example C++ shared library from the cxx-cmake template family, \
built with CMake, linked against OpenSSL and installed with a CMake package \
configuration for downstream find_package(util)."
HOMEPAGE = "https://github.com/SherlockInSpace/cxx-cmake"
LICENSE = "MIT"
# md5sum of LICENSE at SRCREV. Recompute when SRCREV moves.
LIC_FILES_CHKSUM = "file://LICENSE;md5=940a18db9c718275476294053bc4dbb9"

PV = "0.1.0+git"
SRC_URI = "git://github.com/SherlockInSpace/cxx-cmake.git;protocol=https;branch=library"
# Tip of the library branch when this recipe was written.
SRCREV = "e362a7df2b109676672787b02a11eb3b3878c40f"
# No S needed: wrynose unpacks the tree into ${UNPACKDIR}/${BP}, the default.

DEPENDS = "openssl"

inherit cmake pkgconfig

# The option names at this SRCREV. They become BUILD_TESTING/BUILD_DOCS with
# the library's CMake rework.
EXTRA_OECMAKE = "-DBUILD_TEST=OFF -DBUILD_DOC=OFF"
