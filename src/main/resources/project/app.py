from flask import Flask, render_template

app = Flask(__name__)  # Default: templates/ dan static/

@app.route("/")
def home():
    return render_template("tampilanUtama.html")

if __name__ == "__main__":
    app.run(debug=True, port=8080)
