# Route Runner Mobile Client
An Android real-time PvP supply delivery game powered by LibGDX and PubNub.

## Setup IDE
1. Download [Android Studio](https://developer.android.com/sdk/index.html)

## Setup Project
1. Select `Import Project` in Android Studio and click the `build.gradle` at the top-level directory, i.e. `routerunner-mobile/RouteRunner/build.gradle`
2. Go to `SDK Manager` and add APK 21, Google Play Services 
3. Click the green play button
4. Android virtual device configuration
  * Nexus 5
  * Lollipop API 21 x86
5. To rotate on Mac, use Fn+Control+F11


## Testing
1. Right-click any *Test.java file within the `Test` folder and click Run.


## How to Play
- Goal: To get to the target amount of money before the other player
- Create a lobby or join a lobby
- The person with the lexicographically smallest name is blue and starts in the bottom left corner, while the other player is red and starts in the top right corner. Their respective drop-off points for trucks is the UCLA Bruin and the USC Trojan.
- To deliver money to the drop-off point, buy a truck.
- To move the truck, click the truck, click `Edit Route`, plot waypoints within the green radius, click `Snap to Roads` when you are satisfied with your route
- To destroy an opponent's truck, buy a missile and tap on an opponent's truck to destroy it.
- When a truck reaches the drop-off point, your current money will increase and the truck will respawn at your base, i.e. the factory.

## Pivotal Tracker
[Pivotal Tracker](https://www.pivotaltracker.com/n/projects/1445446)

## Resources
* [Game Engine](https://github.com/libGDX/libGDX/wiki)
* [PubNub](https://www.pubnub.com/docs/android-java/pubnub-java-sdk)

