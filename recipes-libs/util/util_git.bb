# The library template at its release tag. From v0.2.0 the library installs
# a versioned libutil.so and its CMake package under ${libdir}/cmake/util,
# so the default packaging splits it cleanly into util and util-dev. Keep it
# that way: a QA failure here is a library bug, not something for
# INSANE_SKIP or FILES.

SUMMARY = "cxx-cmake C++ library template"
DESCRIPTION = "Example C++ shared library from the cxx-cmake template family, \
built with CMake, linked against OpenSSL and installed with a CMake package \
configuration for downstream find_package(util)."
HOMEPAGE = "https://github.com/SherlockInSpace/cxx-cmake-library"
LICENSE = "MIT"
# md5sum of LICENSE at SRCREV. Recompute when SRCREV moves.
LIC_FILES_CHKSUM = "file://LICENSE;md5=940a18db9c718275476294053bc4dbb9"

PV = "0.2.0"
# tag= makes the fetcher check that v${PV} still points at SRCREV.
SRC_URI = "git://github.com/SherlockInSpace/cxx-cmake-library.git;protocol=https;branch=main;tag=v${PV}"
SRCREV = "019a035251b16387223365bf72dc15b8c8b9a813"
# No S needed: wrynose unpacks the tree into ${UNPACKDIR}/${BP}, the default.

DEPENDS = "openssl"

inherit cmake pkgconfig

# Docs and benchmarks are off by default; the tests come back with ptest.
EXTRA_OECMAKE = "-DBUILD_TESTING=OFF"
