#!/bin/bash
#
# Copyright (C) 2022 The LineageOS Project
#
# SPDX-License-Identifier: Apache-2.0
#

function blob_fixup() {
    case "${1}" in
        vendor/lib64/vendor.qti.hardware.camera.postproc@1.0-service-impl.so)
            "${SIGSCAN}" -p "13 0A 00 94" -P "1F 20 03 D5" -f "${2}"
            ;;
        vendor/lib64/camera/components/com.qti.node.mialgocontrol.so)
            "${ANDROID_ROOT}"/prebuilts/clang/host/linux-x86/clang-r450784e/bin/llvm-strip --strip-debug "${2}"
            grep -q "libpiex_shim.so" "${2}" || ${PATCHELF} --add-needed "libpiex_shim.so" "${2}"
            ;;
    esac
}

# If we're being sourced by the common script that we called,
# stop right here. No need to go down the rabbit hole.
if [ "${BASH_SOURCE[0]}" != "${0}" ]; then
    return
fi

set -e

export DEVICE=spes
export DEVICE_COMMON=sm6225-common
export VENDOR=xiaomi

"./../../${VENDOR}/${DEVICE_COMMON}/extract-files.sh" "$@"
