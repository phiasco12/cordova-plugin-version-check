// File: scripts/add-jsoup-hook.js

module.exports = function(context) {
    var fs = require('fs');
    var path = require('path');

    var gradlePath = path.join(context.opts.projectRoot, 'platforms/android/app/build.gradle');

    // Add JSoup dependency if it doesn't already exist
    var gradleFileContent = fs.readFileSync(gradlePath, 'utf8');
    if (!gradleFileContent.includes("org.jsoup:jsoup:1.13.1")) {
        var newDependency = "implementation 'org.jsoup:jsoup:1.13.1'\n";
        gradleFileContent = gradleFileContent.replace(/dependencies {/, `dependencies {\n    ${newDependency}`);
        fs.writeFileSync(gradlePath, gradleFileContent, 'utf8');
    }
};
