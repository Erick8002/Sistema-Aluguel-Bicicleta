const BASE_URL = "/alugueis";

async function request(url = "", options = {}) {
  const response = await fetch(`${BASE_URL}${url}`, {
    headers: {
      "Content-Type": "application/json",
      ...(options.headers || {}),
    },
    ...options,
  });

  if (!response.ok) {
    let mensagem = `Erro HTTP ${response.status}`;
    try {
      const data = await response.json();
      mensagem = data.message || data.error || JSON.stringify(data);
    } catch {
      const texto = await response.text();
      if (texto) mensagem = texto;
    }
    throw new Error(mensagem);
  }

  if (response.status === 204) return null;
  return response.json();
}

export function listarAlugueis() {
  return request("");
}

export function criarAluguel(payload) {
  return request("", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export function devolverAluguel(id) {
  return request(`/${id}/devolucao`, {
    method: "POST",
  });
}