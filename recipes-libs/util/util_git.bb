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

PV = "0.3.0"
# tag= makes the fetcher check that v${PV} still points at SRCREV.
SRC_URI = "git://github.com/SherlockInSpace/cxx-cmake-library.git;protocol=https;branch=main;tag=v${PV} \
           file://run-ptest"
SRCREV = "02ded6a76ffc0ddecbb74e62c695e6abdee0c824"
# No S needed: wrynose unpacks the tree into ${UNPACKDIR}/${BP}, the default.

DEPENDS = "openssl"

inherit cmake pkgconfig ptest

# The unit tests build only for util-ptest. googletest is a static library
# in meta-oe, so the test binary needs nothing extra at run time. The
# functional tier skips itself when cross compiling, and docs and benchmarks
# are off by default.
PACKAGECONFIG ??= "${@bb.utils.contains('PTEST_ENABLED', '1', 'ptest', '', d)}"
PACKAGECONFIG[ptest] = "-DBUILD_TESTING=ON,-DBUILD_TESTING=OFF,googletest"

# Runs on the target only, through ptest-runner on a board. Nothing here
# executes under emulation.
do_install_ptest() {
    install -m 0755 ${B}/test/unit/util_unit_tests ${D}${PTEST_PATH}/
}
