#import "ReactNativeMTBeaconPlus.h"
#ifdef REACT_NATIVE_MTBEACON_PLUS_SWIFT
@import react_native_mtbeacon_plus_swift;
#else
#import "ReactNativeMTBeaconPlus-Swift.h"
#endif

@interface ReactNativeMTBeaconPlus () <MTBeaconPlusManagerDelegate>
@property(nonatomic) MTBeaconPlusManager* manager;
@end

@implementation ReactNativeMTBeaconPlus {
    bool hasListeners;
}


RCT_EXPORT_MODULE(MTBeaconPlusManager)

- (NSArray<NSString *> *)supportedEvents {
    return MTBeaconPlusEvent.events;
}

- (void)dispatchEvent:(NSString * _Nonnull)name value:(id _Nonnull)value {
    if (hasListeners) {
        [self sendEventWithName:name body:value];
    }
}

- (void)startObserving {
    hasListeners = YES;
}

- (void)stopObserving {
    hasListeners = NO;
}

- (NSDictionary<NSString *,id> *)constantsToExport {
    NSMutableDictionary* consts = [NSMutableDictionary new];
    for (NSString* event in MTBeaconPlusEvent.events) {
        [consts setValue:event forKey:event];
    }
    return consts;
}

+ (BOOL)requiresMainQueueSetup {
    return YES;
}

RCT_EXPORT_METHOD(createClient) {
    _manager = [[MTBeaconPlusManager alloc] init];
    _manager.delegate = self;
}

RCT_EXPORT_METHOD(destroyClient) {
    [_manager invalidate];
    _manager = nil;

}

- (void)invalidate {
    [self destroyClient];
}


RCT_EXPORT_METHOD(startScan)
{
    // TODO: Implement some actually useful functionality
    [_manager startScan];
}

RCT_EXPORT_METHOD(stopScan) {
    [_manager stopScan];
}

RCT_EXPORT_METHOD(state:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [_manager state:resolve
             reject:reject];
}

@end
