# Image Area Blur Library

A simple library for blurring specific areas of an image provided by a URL.

## Installation

To install the library, use:

npm install image-area-blur


## Usage

Import the library and use the `getBlurredImage` function to blur a specific area of an image.

### Blurring an Image Area

To blur a specific area of an image, use the following code:

```javascript
import { getBlurredImage } from 'image-area-blur';

useEffect(() => {
  const fetchAndLog = async () => {
    const result = await getBlurredImage(
      'https://cdn.jdpower.com/Average%20Weight%20Of%20A%20Car.jpg',
      300,  // x-coordinate of the top-left corner
      300,  // y-coordinate of the top-left corner
      300,  // width of the area to blur
      300   // height of the area to blur
    );
    console.log(result);
  };

  fetchAndLog();
}, []);
```

### Function Parameters:
```javascript
URL (string): URL of the image you want to process.
x (number): x-coordinate of the top-left corner of the area you want to blur.
y (number): y-coordinate of the top-left corner of the area you want to blur.
width (number): width of the area to blur.
height (number): height of the area to blur.
```
The function returns a processed image URL or data URI after blurring the specified area.

## License
This library is licensed under the MIT License. See the LICENSE file for more information.