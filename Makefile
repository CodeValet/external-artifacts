check: vendor/bats
	./vendor/bats/bin/bats $(wildcard t/*.bats) $(wildcard t/**/*.bats)
	./resources/io/codevalet/externalartifacts/upload-file-azure.sh README.adoc

vendor/bats:
	mkdir -p vendor
	git clone --depth 1 \
		-b v0.4.0 \
		https://github.com/sstephenson/bats.git $@


.PHONY: check
