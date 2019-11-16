java_library(
    name = "dungeon",
    srcs = glob(["**/*.java"]),
    deps = [
        "//lib:json.jar",
    ],
    visibility = ["//src:__subpackages__"],
)

java_binary(
    name = "DungeonApplication",
    srcs = glob(["**/*.java"]),
    deps = [
        "//lib:json.jar",
    ],
    resources = glob([
        "scenes/*.fxml",
    ]) + [
        "//dungeons",
        "//images",
        "//thumbnails",
    ],
    runtime_deps = [
        "//lib:json.jar",
    ],
    visibility = ["//src:__subpackages__"],
)

