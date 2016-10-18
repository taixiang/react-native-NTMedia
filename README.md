# react-native-NTMedia

安装：  
    `npm install react-native-ntmedia`  
    `rnpm link react-native-ntmedia`  
      
Android 添加:  
  `找到getPackages方法所在的类`    
  `头部添加import com.ntutilmedia.NTMediaPackage;`   
  `getPackages 方法里添加 new NTMediaPackage()(如下)`  
  `@Override`   
    `protected List<ReactPackage> getPackages() {`  
      `return Arrays.<ReactPackage>asList(`  
         `new MainReactPackage(),`  
         `new NTMediaPackage()`  
      `);`  
    `}`  
      
JS 使用:
    `const mediaModule = NativeModules.NTMediaModule;`  
    `初始化media,传入url地址,返回总时长`  
    `componentDidMount(){`  
    `mediaModule.initMedia(mediaUrl,(e)=>{alert(e)});`  
    `}`  
    `音频播放`  
    `mediaModule.playMedia();`  
    `音频暂停`  
    `mediaModule.pauseMedia();`  
    `释放media资源`
    `componentWillUnmount(){`  
    `mediaModule.releaseMedia();`
    `}`
    `接受播放的时长 type:5001 , currentDuration:播放时长 `
     `DeviceEventEmitter.addListener('NTUtilMedia',(e)=>{`  
    `switch(e.type){`  
      `case 5001:`  
      `break;`  
    `}`  
  `});`
