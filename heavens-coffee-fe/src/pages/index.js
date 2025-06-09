export default function index() {
  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <h1 className="text-4xl font-bold mb-6">Welcome to Heaven's Coffee</h1>
      <p className="text-lg mb-4">Your favorite coffee shop is just a click away!</p>
      <a href="/login" className="bg-blue-500 text-white px-6 py-2 rounded hover:bg-blue-600 transition">
        Login
      </a>
    </div>
  );
}
