//
//  NTMedia.m
//  NTMedia
//
//  Created by 孝辰 吴 on 2016/10/18.
//  Copyright © 2016年 TCJQ. All rights reserved.
//

#import "NTMedia.h"




@implementation NTMedia



@synthesize bridge = _bridge;

RCT_EXPORT_MODULE(NTMediaModule);

RCT_EXPORT_METHOD(initMedia:(NSString *)str andCallback:(RCTResponseSenderBlock)callback){
	
	NSURL *url = [NSURL URLWithString:str];
	
	NSLog(@"url:%@",str);
	
	self.isPlaying = NO;
	self.playerItem = [[AVPlayerItem alloc] initWithURL:url];
	self.player = [[AVPlayer alloc]initWithPlayerItem:self.playerItem];
	//播放完毕后发出通知
	[[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(PlayedidEnd) name:AVPlayerItemDidPlayToEndTimeNotification object:nil];
	//添加观察者，用来监视播放器的状态变化
	[self.playerItem addObserver:self forKeyPath:@"status" options:NSKeyValueObservingOptionNew context:nil];
	
	__weak NTMedia *ntmedia = self;
	__weak AVPlayer *weakPlayer = self.player;
	
	self.timeObserver = [self.player addPeriodicTimeObserverForInterval:CMTimeMake(1, 1) queue:dispatch_get_main_queue() usingBlock:^(CMTime time) {
		//获取当前时间
		float currentTime = weakPlayer.currentItem.currentTime.value/weakPlayer.currentItem.currentTime.timescale;
		
		[ntmedia currentTime:[NSString stringWithFormat:@"%f",currentTime]];
		
	}];
	
	
	
}


RCT_EXPORT_METHOD(playMedia){
	
	
	NSLog(@"isPlaying : %@",self.isPlaying?@"YES":@"NO");
	
	if(self.isPlaying){
		[self.player play];
	}
	
}


RCT_EXPORT_METHOD(pauseMedia){
	if(self.isPlaying){
		[self.player pause];
	}
}

RCT_EXPORT_METHOD(sliderPlayMedia:(int)time){
	[self.player seekToTime:CMTimeMake(time,1)];
}

RCT_EXPORT_METHOD(releaseMedia){
	
	if(self.player!=nil && self.playerItem!=nil && self.timeObserver!=nil){
		[self.playerItem removeObserver:self forKeyPath:@"status" context:nil];
		[self.player removeTimeObserver:self.timeObserver];
		self.player = nil;
		self.playerItem = nil;
		[[NSNotificationCenter defaultCenter] removeObserver:self];
	}
	
}


//观察者方法，用来监听播放器状态
-(void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary<NSKeyValueChangeKey,id> *)change context:(void *)context{
	//当播放器状态改变
	if([keyPath isEqualToString:@"status"]){
		if(self.player.status == AVPlayerStatusReadyToPlay){
			//等到播放器状态变为就绪时，让播放按钮可用
			self.isPlaying = YES;
			[self canPlay];
		}else{
			self.isPlaying = NO;
		}
	}
}

-(void)dealloc{
	
	[self.playerItem removeObserver:self forKeyPath:@"status" context:nil];
	[self.player removeTimeObserver:self.timeObserver];
	[[NSNotificationCenter defaultCenter] removeObserver:self];
	self.player = nil;
	self.playerItem = nil;
}

/**
 播放结束
 */
- (void)PlayedidEnd
{
	[self.bridge.eventDispatcher sendAppEventWithName:@"cl_msg"
												 body:@{@"type": @"5002"}];
}

-(void)currentTime:(NSString *)time{
	[self.bridge.eventDispatcher sendAppEventWithName:@"cl_msg"
												 body:@{@"type": @"5001",@"currentDuration":time}];
}

-(void)canPlay{
	[self.bridge.eventDispatcher sendAppEventWithName:@"cl_msg"
												 body:@{@"type": @"5000"}];
}

@end
