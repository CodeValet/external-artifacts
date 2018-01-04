/*
 * archiveArtifacts- override the built-in `archiveArtifacts` step.
 */

def call(Map args) {
    assert args.artifacts
    String uploadScriptName = '__azure-upload.sh'
    String uploadScript = libraryResource 'io/codevalet/externalartifacts/upload-file-azure.sh'
    writeFile file: uploadScriptName, text: uploadScript
    sh 'ls -lah'
    String uploadedUrl = sh(script: "bash ${uploadScriptName} ${args.artifacts}",
                            returnStdout: true).trim()
    echo uploadedUrl

    if (uploadedUrl =~ /https\:\/\//) {
        /* if the output was a URL, generate our redirect file */
        String redirectFile = "${args.artifacts}.html"
        String redirectHtml = "<html><head><meta http-equiv=\"refresh\" content=\"0;URL=${uploadedUrl}\" /><title>Redirecting...</title></head><body><center>${args.artifacts} can be downloaded <a href=\"${uploadedUrl}\">from Azure</a></center></body></html>"
        writeFile file: redirectFile, text:redirectHtml
        steps.archiveArtifacts redirectFile
    }
}

def call(String artifacts) {
    return call(artifacts: artifacts)
}
