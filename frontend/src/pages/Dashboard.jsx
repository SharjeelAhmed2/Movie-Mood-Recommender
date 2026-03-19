import { useState } from "react";
import MoviesGrid from "../components/MoviesGrid.jsx";

export default function Dashboard() {
  const [mood, setMood] = useState("");
  const [count, setCount] = useState(3);
  const [movies, setMovies] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  async function fetchRecs() {
    if (!mood.trim()) return;
    setLoading(true);
    setError(null);
    try {
      const res = await fetch(`/recommend?count=${count}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ moodText: mood }),
      });
      if (!res.ok) throw new Error("Server error");
      const data = await res.json();
      setMovies(data.movies || []);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="max-w-3xl mx-auto p-6">
      <h1 className="text-3xl font-bold mb-6 text-center">Mood‑Driven Movie Recommender</h1>

      <label className="block mb-2 font-semibold" htmlFor="mood">
        How do you feel?
      </label>
      <textarea
        id="mood"
        rows="3"
        value={mood}
        onChange={(e) => setMood(e.target.value)}
        className="w-full p-3 border rounded focus:outline-none focus:ring focus:border-indigo-500"
        placeholder="Feeling nostalgic and lonely tonight…"
      />

      <div className="flex items-center gap-3 mt-4">
        <label htmlFor="count" className="font-medium">
          # of picks:
        </label>
        <input
          type="number"
          id="count"
          min="1"
          max="10"
          value={count}
          onChange={(e) => setCount(Number(e.target.value))}
          className="w-20 p-1 border rounded focus:outline-none focus:ring focus:border-indigo-500"
        />
        <button
          onClick={fetchRecs}
          disabled={loading}
          className="ml-auto px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-700 disabled:opacity-50"
        >
          {loading ? "Thinking…" : "Get suggestions"}
        </button>
      </div>

      {error && (
        <p className="mt-4 text-red-600">{error}</p>
      )}

      <MoviesGrid movies={movies} />
    </div>
  );
}