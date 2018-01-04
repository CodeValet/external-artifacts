/*
 * archiveArtifacts- override the built-in `archiveArtifacts` step.
 */

def call(Map args) {
    assert args.artifacts
    echo args
}

def call(String artifacts) {
    return call(artifacts: includes)
}
