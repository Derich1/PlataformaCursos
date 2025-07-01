import React, { useState, useRef } from 'react';

interface VideoUploadProps {
  onDurationChange: (minutes: number, seconds: number) => void;
  onFileChange: (file: File) => void;
}

const VideoUpload: React.FC<VideoUploadProps> = ({ onDurationChange, onFileChange }) => {
  const [, setFile] = useState<File | null>(null);
  const videoRef = useRef<HTMLVideoElement>(null);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files?.[0]) {
      const selectedFile = e.target.files[0];
      setFile(selectedFile);
      onFileChange(selectedFile);
      const url = URL.createObjectURL(selectedFile);
      if (videoRef.current) {
        videoRef.current.src = url;
        videoRef.current.load();
        videoRef.current.onloadedmetadata = () => {
          const duration = videoRef.current?.duration ?? 0;
          const minutes = Math.floor(duration / 60);
          const seconds = Math.floor(duration % 60);
          onDurationChange(minutes, seconds);
        };
      }
    }
  };

  return (
    <div>
      <input type="file" accept="video/*" onChange={handleFileChange} required className="w-full text-sm shadow-lg" />
      <video ref={videoRef} style={{ display: 'none' }} />
    </div>
  );
};

export default VideoUpload;