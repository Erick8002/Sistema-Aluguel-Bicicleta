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

export async function listarClientes() {
  const response = await fetch("/clientes");
  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    throw new Error(data.mensagem || data.message || data.error || "Erro ao listar clientes.");
  }
  return data;
}

export async function criarCliente(payload) {
  const response = await fetch("/clientes", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });
  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    throw new Error(data.mensagem || data.message || data.error || "Erro ao cadastrar cliente.");
  }
  return data;
}

export async function deletarCliente(id) {
  const response = await fetch(`/clientes/${id}`, {
    method: "DELETE",
  });
  if (!response.ok) {
    const data = await response.json().catch(() => ({}));
    throw new Error(data.mensagem || data.message || data.error || "Erro ao excluir cliente.");
  }
}

async function requestResource(resource, options = {}) {
  const response = await fetch(resource, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...(options.headers || {}),
    },
  });

  if (!response.ok) {
    const data = await response.json().catch(() => ({}));
    throw new Error(
      data.mensagem || data.message || data.error || `Erro HTTP ${response.status}.`
    );
  }

  if (response.status === 204) return null;
  return response.json();
}

export function listarBicicletas() {
  return requestResource("/bicicletas");
}

export function criarBicicleta(payload) {
  return requestResource("/bicicletas", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export function deletarBicicleta(id) {
  return requestResource(`/bicicletas/${id}`, { method: "DELETE" });
}

export function listarCategoriasBicicleta() {
  return requestResource("/categorias-bicicleta");
}

export function criarCategoriaBicicleta(payload) {
  return requestResource("/categorias-bicicleta", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export function deletarCategoriaBicicleta(id) {
  return requestResource(`/categorias-bicicleta/${id}`, { method: "DELETE" });
}

export function listarHistorico() {
  return requestResource("/historicos");
}