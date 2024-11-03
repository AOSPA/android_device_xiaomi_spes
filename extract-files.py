#!/usr/bin/env -S PYTHONPATH=../../../tools/extract-utils python3
#
# SPDX-FileCopyrightText: 2024 The LineageOS Project
# SPDX-License-Identifier: Apache-2.0

from extract_utils.fixups_blob import (
    blob_fixup,
    blob_fixups_user_type,
)
from extract_utils.fixups_lib import (
    lib_fixup_remove,
    lib_fixups,
    lib_fixups_user_type,
)
from extract_utils.main import (
    ExtractUtils,
    ExtractUtilsModule,
)

namespace_imports = [
    'hardware/xiaomi',
    'vendor/xiaomi/sm6225-common',
]

def lib_fixup_vendor_suffix(lib: str, partition: str, *args, **kwargs):
    return f'{lib}_{partition}' if partition == 'vendor' else None

lib_fixups: lib_fixups_user_type = {
    **lib_fixups,
}

blob_fixups: blob_fixups_user_type = {
    'vendor/etc/init/init.batterysecret.rc': blob_fixup()
        .regex_replace('.*seclabel u:r:batterysecret:s0\n', ''),
    'vendor/lib64/hw/fingerprint.goodix.default.so': blob_fixup()
        .patchelf_version('0_17_2')
        .fix_soname(),
    'vendor/lib64/camera/components/com.qti.node.mialgocontrol.so': blob_fixup()
        .strip_debug_sections()
        .add_needed('libpiex_shim.so'),
    'vendor/lib64/libgoodixhwfingerprint.so': blob_fixup()
        .patchelf_version('0_17_2')
        .replace_needed(
            "libvendor.goodix.hardware.biometrics.fingerprint@2.1.so",
            "vendor.goodix.hardware.biometrics.fingerprint@2.1.so"
        ),
    'vendor/lib64/vendor.qti.hardware.camera.postproc@1.0-service-impl.so': blob_fixup()
        .sig_replace('13 0A 00 94', '1F 20 03 D5'),
    ('vendor/lib/android.hardware.camera.provider@2.4-legacy.so', 'vendor/lib64/android.hardware.camera.provider@2.4-legacy.so'): blob_fixup()
        .add_needed('libcamera_provider_shim.so'),
}  # fmt: skip

module = ExtractUtilsModule(
    'spes',
    'xiaomi',
    blob_fixups=blob_fixups,
    lib_fixups=lib_fixups,
    namespace_imports=namespace_imports,
)

if __name__ == '__main__':
    utils = ExtractUtils.device_with_common(module, 'sm6225-common', module.vendor)
    utils.run()
