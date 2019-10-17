java_binary(
    name = "DungeonApplication",
    srcs = glob(["*.java"]),
    deps = [
        "//lib:json.jar",
        #"@maven//:org_openjfx_javafx_controls",
        #"@maven//:org_openjfx_javafx_graphics",
        #"@maven//:org_openjfx_javafx_fxml",
        #"@maven//:org_openjfx_javafx_base",
    ],
    resources = [
        ":DungeonView.fxml",
        "//dungeons",
        "//images",
    ],
    runtime_deps = [
        "//lib:json.jar",
        #"@maven//:org_openjfx_javafx_controls_mac",
        #"@maven//:org_openjfx_javafx_graphics_mac",
        #"@maven//:org_openjfx_javafx_fxml_mac",
        #"@maven//:org_openjfx_javafx_base_mac",
    ],
)
