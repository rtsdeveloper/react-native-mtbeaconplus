# /bin/sh
if [ "$ARCHS" = "x86_64" ]; then
  \cp -rf "$PODS_TARGET_SRCROOT/ios/frameworks/x86/" "$PODS_TARGET_SRCROOT/ios/"
else
  \cp -rf "$PODS_TARGET_SRCROOT/ios/frameworks/arm/" "$PODS_TARGET_SRCROOT/ios/"
fi

echo "Copy $ARCHS frameworks successfully!"
