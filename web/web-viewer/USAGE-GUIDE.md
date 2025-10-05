# 🌐 Web Viewer - Usage Guide

## ✅ Project Complete!

Your Web Viewer is now running at **http://localhost:5173/**

## What's Working

### 1. **Processed Frame Display**
   - The app displays a sample processed frame
   - Currently using a placeholder (gray frame with text)
   - Automatic fallback to external placeholder if local image not found

### 2. **Stats Overlay**
   - **FPS**: Simulated real-time updates (28-32 FPS)
   - **Resolution**: 1920x1080
   - **Timestamp**: Updates every second
   - Positioned in top-left corner with semi-transparent background

### 3. **TypeScript Setup**
   - ✅ Full TypeScript configuration
   - ✅ Type-safe React components
   - ✅ Interface definitions for FrameStats
   - ✅ No TypeScript errors

### 4. **DOM Updates**
   - Uses React `useState` hook for state management
   - Uses `useEffect` hook for interval-based updates
   - Simulates FPS variation (28-32 range)
   - Updates timestamp every second

## Adding Your Own Processed Frame

Replace the placeholder with your actual processed frame:

### Option 1: JPG/PNG Image
```bash
# Copy your frame to the public folder
Copy-Item "path\to\your\frame.jpg" "d:\edgeweb\web-viewer\public\sample-frame.jpg"
```

Then update `App.tsx` line 31:
```tsx
src="/sample-frame.jpg"  // Change from .jpg to your format
```

### Option 2: Multiple Frames
You can extend the app to cycle through frames:

```tsx
const frames = [
  '/frame-1.jpg',
  '/frame-2.jpg',
  '/frame-3.jpg'
]

const [currentFrame, setCurrentFrame] = useState(0)

useEffect(() => {
  const interval = setInterval(() => {
    setCurrentFrame(prev => (prev + 1) % frames.length)
  }, 1000)
  return () => clearInterval(interval)
}, [])
```

## Project Features Demonstrated

### TypeScript
- ✅ Interface definitions (`FrameStats`)
- ✅ Type-safe props and state
- ✅ Proper TypeScript configuration (tsconfig.json)
- ✅ Type-safe event handlers

### React
- ✅ Functional components
- ✅ useState for state management
- ✅ useEffect for side effects
- ✅ Event handling (image error)
- ✅ Conditional rendering

### DOM Updates
- ✅ Real-time stats updates
- ✅ Interval-based updates (setInterval)
- ✅ Component lifecycle management
- ✅ Cleanup on unmount

### Styling
- ✅ CSS positioning (absolute overlay)
- ✅ Responsive design
- ✅ Dark theme
- ✅ CSS backdrop filter effects
- ✅ Flexbox layout

## Development Commands

```bash
# Start dev server
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview

# Run linter
npm run lint
```

## Project Structure

```
web-viewer/
├── src/
│   ├── App.tsx              # Main component
│   │   ├── FrameStats interface
│   │   ├── useState for stats
│   │   ├── useEffect for updates
│   │   └── JSX with overlay
│   ├── App.css              # Component styles
│   ├── main.tsx             # React entry point
│   └── index.css            # Global styles
├── public/
│   ├── sample-frame.jpg     # Your processed frame
│   └── vite.svg             # Vite logo
├── index.html               # HTML template
├── package.json             # Dependencies
├── tsconfig.json            # TypeScript config
└── vite.config.ts           # Vite config
```

## Key Code Sections

### TypeScript Interface (App.tsx)
```tsx
interface FrameStats {
  fps: number
  resolution: string
  timestamp: string
}
```

### State Management
```tsx
const [frameStats, setFrameStats] = useState<FrameStats>({
  fps: 30,
  resolution: '1920x1080',
  timestamp: new Date().toLocaleTimeString()
})
```

### DOM Updates via useEffect
```tsx
useEffect(() => {
  const interval = setInterval(() => {
    setFrameStats(prev => ({
      ...prev,
      fps: Math.floor(28 + Math.random() * 4),
      timestamp: new Date().toLocaleTimeString()
    }))
  }, 1000)
  return () => clearInterval(interval)
}, [])
```

### Stats Overlay
```tsx
<div className="frame-stats">
  <div className="stat-item">
    <span className="stat-label">FPS:</span>
    <span className="stat-value">{frameStats.fps}</span>
  </div>
  {/* ... more stats ... */}
</div>
```

## Customization Ideas

### 1. Connect to Real Data
Replace simulated FPS with actual data from WebSocket or API:

```tsx
useEffect(() => {
  const ws = new WebSocket('ws://your-server:8080')
  ws.onmessage = (event) => {
    const data = JSON.parse(event.data)
    setFrameStats(data)
  }
  return () => ws.close()
}, [])
```

### 2. Add More Stats
Extend the `FrameStats` interface:

```tsx
interface FrameStats {
  fps: number
  resolution: string
  timestamp: string
  frameCount: number        // New
  processingTime: number    // New
  bufferSize: number        // New
}
```

### 3. Toggle Stats Visibility
Add a button to show/hide stats:

```tsx
const [showStats, setShowStats] = useState(true)

// In JSX
{showStats && <div className="frame-stats">...</div>}
```

## Next Steps

1. ✅ **Done**: Basic frame viewer with overlay
2. 🔄 **Optional**: Connect to Android app via WebSocket
3. 🔄 **Optional**: Add frame playback controls
4. 🔄 **Optional**: Add frame analysis tools (histogram, etc.)
5. 🔄 **Optional**: Export frames or stats

## Troubleshooting

### Image not loading?
- Check that the file exists in `public/sample-frame.jpg`
- Check browser console for errors
- Verify image path in `App.tsx`

### Stats not updating?
- Check browser console for JavaScript errors
- Verify useEffect cleanup is working
- Check that the component is mounted

### TypeScript errors?
- Run `npm run build` to check for type errors
- Ensure all imports are correct
- Check that interfaces match usage

## Technologies Used

- **React 19**: Latest React with improved performance
- **TypeScript 5.9**: Type-safe development
- **Vite 5.4**: Fast build tool and dev server
- **ESLint**: Code quality and consistency
- **CSS3**: Modern styling with backdrop-filter

## Performance Notes

- Stats update every 1000ms (configurable)
- Single useEffect hook for all updates
- Proper cleanup prevents memory leaks
- Image lazy loading with error fallback
- Optimized for 60 FPS rendering

---

**Status**: ✅ Fully Functional
**URL**: http://localhost:5173/
**Last Updated**: October 5, 2025
