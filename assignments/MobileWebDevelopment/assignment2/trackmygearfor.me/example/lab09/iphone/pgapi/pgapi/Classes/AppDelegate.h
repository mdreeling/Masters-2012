//
//  AppDelegate.h
//  pgapi
//
//  Created by Richard Rodger on 20/03/2012.
//  Copyright Ricebridge 2012. All rights reserved.
//

#import <UIKit/UIKit.h>
#ifdef PHONEGAP_FRAMEWORK
	#import <cordova/CDVCommandDelegate.h>
#else
	#import "CDVCommandDelegate.h"
#endif

@interface AppDelegate : CDVCommandDelegate {

	NSString* invokeString;
}

// invoke string is passed to your app on launch, this is only valid if you 
// edit pgapi.plist to add a protocol
// a simple tutorial can be found here : 
// http://iphonedevelopertips.com/cocoa/launching-your-own-application-via-a-custom-url-scheme.html

@property (copy)  NSString* invokeString;

@end

