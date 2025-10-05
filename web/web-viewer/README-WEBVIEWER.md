# 🌐 Web Viewer - Processed Frame Display

A minimal TypeScript + React web application that displays a processed frame with real-time statistics overlay.

## Features

- ✅ **Static Frame Display**: Shows a sample processed frame (with fallback placeholder)
- ✅ **Stats Overlay**: Real-time FPS, resolution, and timestamp display
- ✅ **TypeScript Setup**: Fully typed React components with interfaces
- ✅ **DOM Updates**: Dynamic stats updates using React hooks
- ✅ **Modern Stack**: Vite + React + TypeScript

## Project Structure

```
web-viewer/
├── src/
│   ├── App.tsx          # Main component with frame viewer
│   ├── App.css          # Styled frame container & overlay
│   ├── main.tsx         # React entry point
│   └── index.css        # Global styles
├── public/
│   └── sample-frame.jpg # Place your processed frame here
├── index.html           # HTML entry point
├── package.json         # Dependencies
├── tsconfig.json        # TypeScript config
└── vite.config.ts       # Vite bundler config
```

## Quick Start

### Install Dependencies
```bash
npm install
```

### Run Development Server
```bash
npm run dev
```

Open your browser to `http://localhost:5173`

### Build for Production
```bash
npm run build
```

### Preview Production Build
```bash
npm run preview
```

## Adding Your Processed Frame

1. Save your processed frame from the Android app
2. Place it as `public/sample-frame.jpg`
3. The app will automatically display your frame

## Technologies

- **TypeScript**: Type-safe development
- **React 19**: Modern UI framework with hooks
- **Vite**: Lightning-fast build tool
- **ESLint**: Code quality and consistency

## Features Demonstrated

1. **TypeScript Project Setup**: Proper tsconfig, types, and interfaces
2. **React Components**: Functional components with hooks
3. **DOM Updates**: Real-time stat updates using `useState` and `useEffect`
4. **CSS Overlay**: Positioned stats overlay on frame
5. **Error Handling**: Fallback image if frame not found

## Stats Display

The overlay shows:
- **FPS**: Simulated frame rate (28-32 FPS)
- **Resolution**: Frame dimensions (1920x1080)
- **Timestamp**: Last update time

## License

MIT
