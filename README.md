# EvoFunc

Render a constantly evolving set of composed functions. Color algorithm mutates alongside functions and function parameters.

<img src="https://raw.githubusercontent.com/kennycason/evofunc/refs/heads/main/samples/iteration_1728715656_0.png" width="50%"/><img src="https://raw.githubusercontent.com/kennycason/evofunc/refs/heads/main/samples/iteration_1728722276_200.png" width="50%"/>
<img src="https://raw.githubusercontent.com/kennycason/evofunc/refs/heads/main/samples/iteration_1728722000_2200.png" width="50%"/><img src="https://raw.githubusercontent.com/kennycason/evofunc/refs/heads/main/samples/iteration_1728721472_0.png" width="50%"/>
<img src="https://raw.githubusercontent.com/kennycason/evofunc/refs/heads/main/samples/iteration_1728718403_100.png" width="50%"/><img src="https://raw.githubusercontent.com/kennycason/evofunc/refs/heads/main/samples/iteration_1728722420_100.png" width="50%"/>
<img src="https://raw.githubusercontent.com/kennycason/evofunc/refs/heads/main/samples/iteration_1729313889_89.png" width="50%"/><img src="https://raw.githubusercontent.com/kennycason/evofunc/refs/heads/main/samples/iteration_1729313922_307.png" width="50%"/>
<img src="https://raw.githubusercontent.com/kennycason/evofunc/refs/heads/main/samples/iteration_1729317878_860.png" width="50%"/><img src="https://raw.githubusercontent.com/kennycason/evofunc/refs/heads/main/samples/iteration_1729317832_592.png" width="50%"/>
<img src="https://raw.githubusercontent.com/kennycason/evofunc/refs/heads/main/samples/iteration_1728720385_700.png" width="50%"/><img src="https://raw.githubusercontent.com/kennycason/evofunc/refs/heads/main/samples/iteration_1728973553_4000.png" width="50%"/>
<img src="https://raw.githubusercontent.com/kennycason/evofunc/refs/heads/main/samples/iteration_1728973332_1900.png" width="50%"/><img src="https://raw.githubusercontent.com/kennycason/evofunc/refs/heads/main/samples/iteration_1728973458_3100.png" width="50%"/>
<img src="https://raw.githubusercontent.com/kennycason/evofunc/refs/heads/main/samples/iteration_1729109758_100.png" width="50%"/><img src="https://raw.githubusercontent.com/kennycason/evofunc/refs/heads/main/samples/iteration_1729109803_200.png" width="50%"/>
<img src="https://raw.githubusercontent.com/kennycason/evofunc/refs/heads/main/samples/iteration_1729226408_6939.png" width="50%"/><img src="https://raw.githubusercontent.com/kennycason/evofunc/refs/heads/main/samples/iteration_1729228963_190.png" width="50%"/>
<img src="https://raw.githubusercontent.com/kennycason/evofunc/refs/heads/main/samples/iteration_1729229357_1980.png" width="50%"/><img src="https://raw.githubusercontent.com/kennycason/evofunc/refs/heads/main/samples/iteration_1729229465_2430.png" width="50%"/>
<img src="https://raw.githubusercontent.com/kennycason/evofunc/refs/heads/main/samples/iteration_1729229879_4040.png" width="50%"/><img src="https://raw.githubusercontent.com/kennycason/evofunc/refs/heads/main/samples/iteration_1729232157_760.png" width="50%"/>

## Videos

- https://www.youtube.com/watch?v=bDJs5xCmJcs

## Images to Video

`ffmpeg -framerate 8 -pattern_type glob -i "*.png" -vf "scale=1280:720,fps=8" -c:v libx264 -b:v 5000k -crf 16 -pix_fmt yuv420p functions.mp4`
