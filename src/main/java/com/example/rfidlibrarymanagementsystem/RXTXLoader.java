//package com.example.rfidlibrarymanagementsystem;
//
//import gnu.io.RXTXVersion;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.lang.reflect.Field;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.regex.Pattern;
//import org.apache.commons.io.FileUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//public class RXTXLoader {
//    private static final Logger LOG = LoggerFactory.getLogger(RXTXLoader.class);
//    private static final Pattern CPU_INFO_MODEL_INFO = Pattern.compile("model name\\s+:\\s+(.*)");
//    private static final File CPU_INFO_FILE = new File("/proc/cpuinfo");
//
//    public RXTXLoader() {
//    }
//
//    public static void load() throws IOException {
//        load(com.jamierf.rxtx.RXTXLoader.OperatingSystem.get(), com.jamierf.rxtx.RXTXLoader.Architecture.get());
//    }
//
//    public static void load(OperatingSystem os, Architecture arch) throws IOException {
//        File tempDir = createTempDirectory();
//        InputStream source = openResource(os, arch);
//        if (source == null) {
//            throw new IllegalStateException(String.format("Unable to find resource for %s %s", arch, os));
//        } else {
//            File target = new File(tempDir, os.getLibPath());
//            LOG.debug("Loading RXTX library for {} {}", arch, os);
//
//            try {
//                FileUtils.copyInputStreamToFile(source, target);
//                addDirToLoadPath(tempDir);
//            } finally {
//                source.close();
//            }
//
//            String version = RXTXVersion.nativeGetVersion();
//            LOG.info("Loaded RXTX native library {} for {} {}", new Object[]{version, arch, os});
//        }
//    }
//
//    private static InputStream openResource(OperatingSystem os, Architecture arch) throws IOException {
//        String path = String.format("%s/%s/%s", os.getName(), arch.getName(), os.getLibPath());
//        LOG.trace("Loading native library from {}", path);
//        return RXTXLoader.class.getResourceAsStream(path);
//    }
//
//    private static File createTempDirectory() {
//        File tempDir = new File(FileUtils.getTempDirectory(), "rxtx-loader");
//        if (!tempDir.exists()) {
//            tempDir.mkdir();
//        }
//
//        return tempDir;
//    }
//
//    private static void addDirToLoadPath(File dir) throws IOException {
//        String path = dir.getPath();
//
//        try {
//            Field field = ClassLoader.class.getDeclaredField("usr_paths");
//            boolean accessible = field.isAccessible();
//            field.setAccessible(true);
//            String[] existingPaths = (String[])((String[])field.get((Object)null));
//            Set<String> newPaths = new HashSet(existingPaths.length + 1);
//            newPaths.addAll(Arrays.asList(existingPaths));
//            if (!newPaths.contains(path)) {
//                newPaths.add(path);
//                field.set((Object)null, newPaths.toArray(new String[newPaths.size()]));
//                field.setAccessible(accessible);
//            }
//        } catch (IllegalAccessException var6) {
//            throw new IOException("Failed to get permissions to set library path", var6);
//        } catch (NoSuchFieldException var7) {
//            throw new IOException("Failed to get field handle to set library path", var7);
//        }
//    }
//}
//
