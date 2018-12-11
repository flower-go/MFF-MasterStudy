#!/usr/bin/env python3

def extract():
    import base64
    import io
    import tarfile
    with io.BytesIO(base64.b85decode(data)) as tar_data:
        with tarfile.open(fileobj=tar_data, mode="r") as tar_file:
            tar_file.extractall()

if __name__ == "__main__":
    extract()