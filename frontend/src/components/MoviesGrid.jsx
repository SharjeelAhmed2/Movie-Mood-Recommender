export default function MoviesGrid({ movies = [] }) {
  if (!movies.length) return null;
  return (
    <div className="grid gap-6 mt-8 sm:grid-cols-2 md:grid-cols-3">
      {movies.map((m) => (
        <div
          key={m.id}
          className="border rounded overflow-hidden shadow hover:shadow-lg transition-shadow"
        >
          <img
            src={m.posterUrl}
            alt={m.title}
            className="w-full h-64 object-cover"
          />
          <div className="p-3">
            <p className="font-semibold">{m.title}</p>
            <p className="text-sm text-gray-500">{m.year}</p>
          </div>
        </div>
      ))}
    </div>
  );
}