# Tracy's client library, for building the app with profiling zones. Shared,
# so every library in a process uses one profiler, and on demand, so nothing
# is recorded until a capture tool connects. Capturing runs on the host, so
# none of Tracy's tools are built here. Its version has to match the
# tracy-capture in cxx-cmake-container's images.

SUMMARY = "Tracy profiler client library"
HOMEPAGE = "https://github.com/wolfpld/tracy"
LICENSE = "BSD-3-Clause"
# md5sum of LICENSE at SRCREV. Recompute when SRCREV moves.
LIC_FILES_CHKSUM = "file://LICENSE;md5=731851511b7ea8ad0fbdf41b9c4f5ed9"

SRC_URI = "git://github.com/wolfpld/tracy.git;protocol=https;branch=master;tag=v${PV}"
SRCREV = "f2a4f2529e7e71844c9e434b877770a21ecf7587"

inherit cmake

# TRACY_ENABLE and TRACY_ON_DEMAND reach consumers through the
# Tracy::TracyClient target, so the app compiles against the same settings.
EXTRA_OECMAKE = "-DBUILD_SHARED_LIBS=ON -DTRACY_ENABLE=ON -DTRACY_ON_DEMAND=ON"
