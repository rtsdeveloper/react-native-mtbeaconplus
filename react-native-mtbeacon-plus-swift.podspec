require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-mtbeacon-plus-swift"
  s.version      = package["version"]
  s.summary      = package["description"]

  s.homepage     = "https://github.com/rtsdeveloper/rn-mtbeaconplus"
  s.license      = "MIT"
  # s.license    = { :type => "MIT", :file => "FILE_LICENSE" }
  s.authors      = { "RTS-Developer" => "sharmaritesh9794@gmail.com" }
  s.platforms    = { :ios => "9.0" }
  s.source       = { :git => "https://github.com/rtsdeveloper/rn-mtbeaconplus.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{swift}"
  s.vendored_frameworks = "ios/MTBeaconPlus.framework", "ios/iOSDFULibrary.framework", "ios/ZIPFoundation.framework"
  s.pod_target_xcconfig = { 'SWIFT_VERSION' => '4.1' }

  s.script_phase = {
    :name => 'Copy frameworks before compile',
    :script => '${PODS_TARGET_SRCROOT}/ios/frameworks/archs.sh',
    :execution_position => :before_compile
  }
  
end

