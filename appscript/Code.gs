/**
 * CRUD de constelaciones: Apps Script (frontend) -> Spring Boot (REST).
 * Las llamadas al backend se hacen desde el servidor con UrlFetchApp,
 * así el navegador no necesita CORS.
 */
const API_URL = 'https://actividad.sirioautomatiza.com/api/constelaciones';

const MENSAJES_ERROR = {
  400: 'Datos inválidos: revisa los campos del formulario.',
  404: 'La constelación ya no existe.'
};

function doGet() {
  return HtmlService.createHtmlOutputFromFile('Index')
    .setTitle('Catálogo de constelaciones')
    .addMetaTag('viewport', 'width=device-width, initial-scale=1');
}

// ---- Funciones invocadas desde el cliente con google.script.run ----

function listarConstelaciones() {
  return llamarApi_('get');
}

function crearConstelacion(datos) {
  return llamarApi_('post', null, datos);
}

function actualizarConstelacion(id, datos) {
  return llamarApi_('put', id, datos);
}

function eliminarConstelacion(id) {
  llamarApi_('delete', id);
  return { ok: true };
}

// ---- Utilidades ----

function llamarApi_(metodo, id, cuerpo) {
  const opciones = { method: metodo, muteHttpExceptions: true };
  if (cuerpo) {
    opciones.contentType = 'application/json';
    opciones.payload = JSON.stringify({
      nombre: cuerpo.nombre,
      hemisferio: cuerpo.hemisferio,
      estrellaPrincipal: cuerpo.estrellaPrincipal,
      descubiertoPor: cuerpo.descubiertoPor
    });
  }

  const url = id ? API_URL + '/' + encodeURIComponent(id) : API_URL;
  const respuesta = UrlFetchApp.fetch(url, opciones);
  const codigo = respuesta.getResponseCode();

  if (codigo >= 400) {
    throw new Error(MENSAJES_ERROR[codigo] || 'El servidor respondió con el error ' + codigo + '.');
  }

  const texto = respuesta.getContentText();
  return texto ? JSON.parse(texto) : null;
}
