/*
 * archive - override the built-in `archive` step.
 *
 * This is just a support shim to pass off to the "real" step worth overriding:
 * archiveArtifacts
 */

def call(Map args) {
    assert args.includes
    return archiveArtifacts(artifacts: args.includes, excludes: args.excludes)
}

def call(String includes) {
    return call(includes: includes)
}
