/*
 * archiveArtifacts- override the built-in `archiveArtifacts` step.
 */

def call(Map args) {
    assert args.artifacts
    String uploadScriptName = '__azure-upload.sh'
    String uploadScript = libraryResource 'io/codevalet/externalartifacts/upload-file-azure.sh'
    writeFile file: uploadScriptName, text: uploadScript
    sh 'ls -lah'
    sh "bash ${uploadScriptName}"
}

def call(String artifacts) {
    return call(artifacts: artifacts)
}
