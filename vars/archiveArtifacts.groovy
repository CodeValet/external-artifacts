/*
 * archiveArtifacts- override the built-in `archiveArtifacts` step.
 */

def call(Map args) {
    assert args.artifacts

    if (args.artifacts =~ /\*/) {
        error "archiveArtifacts with external storage doesn't support patterns right now"
    }

    String propertyName = 'hudson.model.DirectoryBrowserSupport.CSP'
    /* Mess with Jenkins' Content-Security Policy settings to allow our
     * redirects, gnarly
     */
    if (null == System.getProperty(propertyName)) {
        System.setProperty(propertyName,
            'sandbox allow-scripts allow-same-origin;')
    }
    String uploadScriptName = '__azure-upload.sh'
    String uploadScript = libraryResource 'io/codevalet/externalartifacts/upload-file-azure.sh'
    writeFile file: uploadScriptName, text: uploadScript

    String uploadedUrl
    withCredentials([string(credentialsId: 'azure-access-key',
        variable: 'AZURE_ACCESS_KEY')]) {

        uploadedUrl = sh(script: "bash ${uploadScriptName} ${args.artifacts}",
                            returnStdout: true).trim()
    }

    if (uploadedUrl =~ /https\:\/\//) {
        /* if the output was a URL, generate our redirect file */
        String redirectFile = "${args.artifacts}.html"
        String redirectHtml = "<html><head><meta http-equiv=\"refresh\" content=\"0;URL=${uploadedUrl}\" /><title>Redirecting...</title></head><body><center>${args.artifacts} can be downloaded <a href=\"${uploadedUrl}\">from Azure</a></center></body></html>"
        writeFile file: redirectFile, text:redirectHtml
        steps.archiveArtifacts redirectFile
    }
    else  {
        echo uploadedUrl
    }
}

def call(String artifacts) {
    return call(artifacts: artifacts)
}
