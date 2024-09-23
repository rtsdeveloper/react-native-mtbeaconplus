require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "rn-mtbeaconplus"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  rn-mtbeaconplus
                   DESC
  s.homepage     = "https://github.com/github_account/rn-mtbeaconplus"
  s.license      = "MIT"
  # s.license    = { :type => "MIT", :file => "FILE_LICENSE" }
  s.authors      = { "Groove Developer" => "groove.app.developer@gmail.com" }
  s.platforms    = { :ios => "9.0" }
  s.source       = { :git => "https://github.com/github_account/rn-mtbeaconplus.git", :tag => "#{s.version}" }

  s.source_files = "ios/*.{h,m}"
  s.requires_arc = true
  s.compiler_flags = '-DREACT_NATIVE_MTBEACON_PLUS_SWIFT'

  s.dependency 'React'
  s.dependency 'react-native-mtbeacon-plus-swift'
  # ...
  # s.dependency "..."
end

