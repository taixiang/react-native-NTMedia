//
//  NTMedia.h
//  NTMedia
//
//  Created by 孝辰 吴 on 2016/10/18.
//  Copyright © 2016年 TCJQ. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "RCTBridgeModule.h"
#import "RCTBridge.h"
#import "RCTEventDispatcher.h"
#import <AVFoundation/AVFoundation.h>

@interface NTMedia : NSObject<RCTBridgeModule>

@property (nonatomic,strong) AVPlayer *player;

@property (nonatomic,strong) AVPlayerItem *playerItem;

@property (nonatomic,assign) BOOL isPlaying;

@end
