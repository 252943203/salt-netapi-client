package com.suse.saltstack.netapi.calls.modules;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.suse.saltstack.netapi.calls.LocalCall;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * salt.modules.pkg
 */
public class Pkg {

    /**
     * Information about a package as returned by "pkg.search".
     */
    public static class PackageInfo {

        private String summary;

        public String getSummary() {
            return summary;
        }
    }

    /**
     * Package dictionary as returned by "pkg.file_dict".
     */
    public static class PackageDict {

        private List<Object> errors;
        private Map<String, List<String>> packages;

        public List<Object> getErrors() {
            return errors;
        }

        public Map<String, List<String>> getPackages() {
            return packages;
        }
    }

    /**
     * Information about a package as returned by pkg.info_installed and
     * pkg.info_available
     */
    public static class Info {

        private String architecture;
        @SerializedName("build_date")
        private ZonedDateTime buildDate;
        @SerializedName("build_host")
        private String buildHost;
        private String description;
        private String group;
        @SerializedName("install_date")
        private ZonedDateTime installDate;
        private String license;
        private String name;
        @SerializedName("new_features_have_been_added")
        private Optional<String> newFeaturesHaveBeenAdded = Optional.empty();
        private String packager;
        private Optional<String> release = Optional.empty();
        private String relocations;
        private String signature;
        private String size;
        @SerializedName("source")
        private String source;
        private String summary;
        private String url;
        private String vendor;
        private String version;
        private Optional<String> epoch = Optional.empty();

        public String getArchitecture() {
            return architecture;
        }

        public ZonedDateTime getBuildDate() {
            return buildDate;
        }

        public String getBuildHost() {
            return buildHost;
        }

        public String getGroup() {
            return group;
        }

        public String getDescription() {
            return description;
        }

        public ZonedDateTime getInstallDate() {
            return installDate;
        }

        public String getLicense() {
            return license;
        }

        public String getName() {
            return name;
        }

        public Optional<String> getNewFeaturesHaveBeenAdded() {
            return newFeaturesHaveBeenAdded;
        }

        public String getPackager() {
            return packager;
        }

        public Optional<String> getRelease() {
            return release;
        }

        public String getRelocations() {
            return relocations;
        }

        public String getSignature() {
            return signature;
        }

        public String getSize() {
            return size;
        }

        public String getSource() {
            return source;
        }

        public String getSummary() {
            return summary;
        }

        public String getUrl() {
            return url;
        }

        public String getVendor() {
            return vendor;
        }

        public String getVersion() {
            return version;
        }

        public Optional<String> getEpoch() {
            return epoch;
        }
    }

    private Pkg() { }

    public static LocalCall<Map<String, PackageInfo>> search(String criteria) {
        LinkedHashMap<String, Object> args = new LinkedHashMap<>();
        args.put("criteria", criteria);
        return new LocalCall<>("pkg.search", Optional.empty(), Optional.of(args),
                new TypeToken<Map<String, PackageInfo>>(){});
    }

    public static LocalCall<PackageDict> fileDict(String... packages) {
        return new LocalCall<>("pkg.file_dict", Optional.of(Arrays.asList(packages)),
                Optional.empty(), new TypeToken<PackageDict>(){});
    }

    public static LocalCall<Map<String, List<String>>> listPkgs() {
        LinkedHashMap<String, Object> args = new LinkedHashMap<>();
        args.put("versions_as_list", true);
        return new LocalCall<>("pkg.list_pkgs", Optional.empty(), Optional.of(args),
                new TypeToken<Map<String, List<String>>>(){});
    }

    public static LocalCall<Map<String, Info>> infoInstalled(String... packages) {
        return new LocalCall<>("pkg.info_installed", Optional.of(Arrays.asList(packages)),
                Optional.empty(), new TypeToken<Map<String, Info>>(){});
    }

    public static LocalCall<Map<String, Info>> infoAvailable(String... packages) {
        return new LocalCall<>("pkg.info_available", Optional.of(Arrays.asList(packages)),
                Optional.empty(), new TypeToken<Map<String, Info>>(){});
    }

    public static LocalCall<Boolean> upgradeAvailable(String packageName) {
        LinkedHashMap<String, Object> args = new LinkedHashMap<>();
        args.put("package", packageName);
        return new LocalCall<>("pkg.upgrade_available", Optional.empty(), Optional.of(args),
                new TypeToken<Boolean>(){});
    }

    public static LocalCall<String> latestVersion(String packageName) {
        LinkedHashMap<String, Object> args = new LinkedHashMap<>();
        args.put("package", packageName);
        return new LocalCall<>("pkg.latest_version", Optional.empty(), Optional.of(args),
                new TypeToken<String>(){});
    }

    public static LocalCall<Map<String, String>> latestVersion(String firstPackageName,
            String secondPackageName, String... packages) {
        return new LocalCall<>("pkg.latest_version",
                    Optional.of(Arrays.asList(firstPackageName,
                            secondPackageName, packages)),
                Optional.empty(), new TypeToken<Map<String, String>>(){});
    }
}
