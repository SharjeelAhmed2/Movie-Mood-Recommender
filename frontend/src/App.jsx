import { Routes, Route, Navigate } from "react-router-dom";
import Dashboard from "./pages/Dashboard.jsx";

export default function App() {
  return (
    <Routes>
      {/* default route */}
      <Route path="/" element={<Dashboard />} />
      {/* fallback: any unknown path -> dashboard */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}