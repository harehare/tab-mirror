const path = require("path");
const CopyPlugin = require("copy-webpack-plugin");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const isProd = process.env.NODE_ENV === "production";

module.exports = {
    mode: isProd ? "production" : "development",
    entry: isProd
        ? { "tab-mirror-opt": path.resolve(__dirname, "tab-mirror-opt.js") }
        : {
              "tab-mirror-fastopt": path.resolve(
                  __dirname,
                  "tab-mirror-fastopt.js"
              ),
          },
    output: {
        path: path.resolve(__dirname, "."),
        filename: "[name]-bundle.js",
    },
    plugins: [
        new CopyPlugin({
            patterns: [
                {
                    from: path.resolve(__dirname, "../../../../web/assets"),
                    to: path.resolve(__dirname, "assets"),
                },
            ],
        }),
        new HtmlWebpackPlugin({
            template: "../../../../web/index.html",
            inject: "body",
            inlineSource: ".css$",
        }),
    ],
    devServer: {
        port: "8080",
    },
    performance: { hints: false },
    ignoreWarnings: [() => true],
};
