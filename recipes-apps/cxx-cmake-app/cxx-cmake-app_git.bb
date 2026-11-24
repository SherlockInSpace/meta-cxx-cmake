# The application template at its release tag. CPM in the app resolves util
# and spdlog with find_package only, so both come from the sysroot and the
# build downloads nothing. It installs one binary, ${bindir}/app.

SUMMARY = "cxx-cmake application template"
DESCRIPTION = "Example C++ command-line application from the cxx-cmake \
template family, built with CMake against the util library and spdlog."
HOMEPAGE = "https://github.com/SherlockInSpace/cxx-cmake-app"
LICENSE = "MIT"
# md5sum of LICENSE at SRCREV. Recompute when SRCREV moves.
LIC_FILES_CHKSUM = "file://LICENSE;md5=dac26d17e0c8c43b2c229499209d1ce4"

PV = "0.1.0"
# tag= makes the fetcher check that v${PV} still points at SRCREV.
SRC_URI = "git://github.com/SherlockInSpace/cxx-cmake-app.git;protocol=https;branch=main;tag=v${PV}"
SRCREV = "264df206eb854ba8ba38cd55f28dd9ee95292643"

# The app asks for util 0.3.x and spdlog 1.15 or later. meta-oe's spdlog is
# 1.17 and brings fmt with it.
DEPENDS = "util spdlog"

inherit cmake

# The unit tests need GoogleTest and the integration tests drive the binary
# with pytest on the build host, so neither tier is built here.
EXTRA_OECMAKE = "-DBUILD_TESTING=OFF"
