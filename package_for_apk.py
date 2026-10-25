#!/usr/bin/env python3
"""
AVA Studio - Automatic Packaging Script for External APK Converters
This script creates a ZIP archive with index.html placed strictly in the ROOT directory
of the archive (without any nested parent folder). This eliminates the error:
"تعذر تحميل الملفات المضمنة في هذا التطبيق. يرجى إعادة بناء APK باستخدام ملف ZIP يوجد فيه index.html في الجذر"
"""

import os
import zipfile
import sys

def package_web_zip():
    output_dir = "dist"
    output_zip = os.path.join(output_dir, "ava-apk-ready.zip")
    os.makedirs(output_dir, exist_ok=True)

    items_to_zip = ["index.html", "css", "js"]
    
    # Verify index.html exists
    if not os.path.isfile("index.html"):
        print("ERROR: index.html not found in the current directory!")
        sys.exit(1)

    print("Packaging files into:", output_zip)
    with zipfile.ZipFile(output_zip, 'w', zipfile.ZIP_DEFLATED) as zipf:
        for item in items_to_zip:
            if os.path.isfile(item):
                zipf.write(item, arcname=item)
                print(f"  + Added file: {item} -> {item}")
            elif os.path.isdir(item):
                for root, _, files in os.walk(item):
                    for file in files:
                        file_path = os.path.join(root, file)
                        arcname = os.path.relpath(file_path, ".")
                        zipf.write(file_path, arcname=arcname)
                        print(f"  + Added file: {arcname}")

    print("\n✓ Verification of ZIP contents:")
    with zipfile.ZipFile(output_zip, 'r') as zipf:
        names = zipf.namelist()
        if "index.html" in names:
            print("  SUCCESS: 'index.html' is at the ROOT of the zip!")
        else:
            print("  WARNING: 'index.html' is not at the root.")
        for name in names:
            print("   -", name)

    print(f"\nCreated successfully at: {output_zip}")
    print("You can now directly upload this file into your external APK converter app without any errors!")

if __name__ == "__main__":
    package_web_zip()
