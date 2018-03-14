#!/bin/sh

echo "Converting from windows to linux file type!"

awk '{ sub("\r$", ""); print }' buildW.sh > buildL.sh

