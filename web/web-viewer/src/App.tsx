import { useState, useEffect } from 'react'
import './App.css'

interface FrameStats {
  fps: number
  resolution: string
  timestamp: string
}

function App() {
  const [frameStats, setFrameStats] = useState<FrameStats>({
    fps: 30,
    resolution: '1920x1080',
    timestamp: new Date().toLocaleTimeString()
  })

  // Simulate FPS updates
  useEffect(() => {
    const interval = setInterval(() => {
      setFrameStats(prev => ({
        ...prev,
        fps: Math.floor(28 + Math.random() * 4), // Simulate 28-32 FPS
        timestamp: new Date().toLocaleTimeString()
      }))
    }, 1000)

    return () => clearInterval(interval)
  }, [])

  return (
    <div className="app">
      <h1>Web Viewer: Processed Frame</h1>
      <p className="subtitle">TypeScript + React Frame Display with Stats Overlay</p>
      
      <div className="frame-container">
        <img 
          id="processed-frame"
          className="processed-frame" 
          src="/sample_frame.png" 
          alt="Processed Frame"
          onError={(e) => {
            // Fallback to a placeholder if image doesn't exist
            e.currentTarget.src = 'https://via.placeholder.com/1920x1080/1a1a1a/ffffff?text=Sample+Processed+Frame'
          }}
        />
        <div className="frame-stats">
          <div className="stat-item">
            <span className="stat-label">FPS:</span>
            <span className="stat-value">{frameStats.fps}</span>
          </div>
          <div className="stat-item">
            <span className="stat-label">Resolution:</span>
            <span className="stat-value">{frameStats.resolution}</span>
          </div>
          <div className="stat-item">
            <span className="stat-label">Updated:</span>
            <span className="stat-value">{frameStats.timestamp}</span>
          </div>
        </div>
      </div>

      <div className="info-panel">
        <h3>Frame Information</h3>
        <p>This web viewer demonstrates:</p>
        <ul>
          <li>TypeScript project setup with React + Vite</li>
          <li>Dynamic DOM updates using React hooks</li>
          <li>CSS overlay for frame statistics</li>
          <li>Real-time stat updates (FPS simulation)</li>
        </ul>
        <p className="note">
          <strong>Note:</strong> Place your processed frame as <code>public/sample-frame.jpg</code> 
          to display a custom image.
        </p>
      </div>
    </div>
  )
}

export default App
