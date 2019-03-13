/*
 * Copyright 2019 Azavea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.azavea.gdal;

import cz.adamh.utils.NativeUtils;

public class GDALWarp {
        private static final String GDALWARP_BINDINGS_RESOURCE_ELF = "/resources/libgdalwarp_bindings.so";
        private static final String GDALWARP_BINDINGS_RESOURCE_MACHO = "/resources/libgdalwarp_bindings.dylib";
        private static final String GDALWARP_BINDINGS_RESOURCE_DLL = "/resources/gdalwarp_bindings.dll";

        public static final int GDT_Unknown = 0;
        public static final int GDT_Byte = 1;
        public static final int GDT_UInt16 = 2;
        public static final int GDT_Int16 = 3;
        public static final int GDT_UInt32 = 4;
        public static final int GDT_Int32 = 5;
        public static final int GDT_Float32 = 6;
        public static final int GDT_Float64 = 7;
        public static final int GDT_CInt16 = 8;
        public static final int GDT_CInt32 = 9;
        public static final int GDT_CFloat32 = 10;
        public static final int GDT_CFloat64 = 11;
        public static final int GDT_TypeCount = 12;

        public static final int SOURCE = 0;
        public static final int WARPED = 1;

        private static final String ANSI_RESET = "\u001B[0m";
        private static final String ANSI_RED = "\u001B[31m";

        private static native void _init(int size, int copies);

        public static void init(int size, int copies) throws Exception {

                String os = System.getProperty("os.name").toLowerCase();

                // Try to load ELF shared object from JAR file ...
                if (os.contains("linux")) {
                        NativeUtils.loadLibraryFromJar(GDALWARP_BINDINGS_RESOURCE_ELF);
                }
                // Try to Load Mach-O shared object from JAR file ...
                else if (os.contains("mac")) {
                        NativeUtils.loadLibraryFromJar(GDALWARP_BINDINGS_RESOURCE_MACHO);
                }
                // Try to load Windows DLL from JAR file ...
                else if (os.contains("win")) {
                        NativeUtils.loadLibraryFromJar(GDALWARP_BINDINGS_RESOURCE_DLL);
                } else {
                        throw new Exception("Unsupported platform");
                }

                _init(size, copies);
        }

        static {
                try {
                        init(1 << 8, 1 << 4);
                } catch (Exception e) {
                        System.err.println(ANSI_RED + "INITIALIZATION FAILED" + ANSI_RESET);
                }
        }

        public static native void deinit();

        public static native long get_token(String uri, String[] options);

        public static native boolean get_overview_widths_heights(long token, int dataset, int attempts, /* */
                        int[] widths, int heights[]);

        public static native boolean get_crs_proj4(long token, int dataset, int attempts, /* */
                        byte[] crs);

        public static native boolean get_crs_wkt(long token, int dataset, int attempts, /* */
                        byte[] crs);

        public static native boolean get_band_nodata(long token, int dataset, int attempts, /* */
                        int band, double[] nodata, int[] success);

        public static native boolean get_band_data_type(long token, int dataset, int attempts, /* */
                        int band, int[] data_type);

        public static native boolean get_band_count(long token, int dataset, int attempts, /* */
                        int[] band_count);

        public static native boolean get_width_height(long token, int dataset, int attempts, /* */
                        int[] width_height);

        public static native boolean get_data( /* */
                        long token, /* */
                        int dataset, /* */
                        int attemps, /* */
                        int[] src_window, /* */
                        int[] dst_window, /* */
                        int band_number, /* */
                        int type, /* */
                        byte[] data);

        public static native boolean get_transform(long token, int dataset, int attempts, /* */
                        double[] transform);
}
