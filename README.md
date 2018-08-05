# x-mode-challenge

In this assignment I chose to use the Network Provider. Although GPS is more accurate than Network, it's slower, it drains battery power more quickly and it does not work outdoors. Network Location Provider determines user location using cell tower and Wi-Fi signals. It works indoor and outdoors, its faster and uses less battery. Network can provide a more accurate location data when near Wi-Fi signal

The solution was updated to also use the Fused Location Provider to obtain the last known location of the user, when the Network provider is unavailable. Fused Location Provider is a good option because it uses both GPS and Wi-Fi in an optimal way. 
