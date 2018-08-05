# x-mode-challenge

In this assignment I chose to use the Network Provider. Although GPS is more accurate than Network, it's slower, it drains battery power more quickly and it does not work outdoors. Network Location Provider determines user location using cell tower and Wi-Fi signals. It works indoor and outdoors, its faster and uses less battery. Network can provide a more accurate location data when near Wi-Fi signals. Depending on the precision necessary for the application and the availability of the providers a the combination of GPS Provider and Network Provider can be use to provide the optimal experience for the user. In the assignment i used the Network Provider to retrieve the user's location data.

The solution was updated to use the Fused Location provider to obtained the last known location of the user if the Network provider is unavailable. Fused Location Provider is a good option because it uses both GPS and Wi-Fi in an optimal way. 
