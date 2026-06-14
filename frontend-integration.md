# Integración Frontend (Next.js) con Backend (Spring Boot)

Archivos modificados del frontend para alinearse con el backend del proyecto.

---

## `next.config.ts`

```typescript
import type { NextConfig } from 'next';

const nextConfig: NextConfig = {
  async rewrites() {
    const apiUrl = process.env.API_URL || 'http://localhost:8080';
    return [
      {
        source: '/api/chat/:path*',
        destination: `${apiUrl}/api/chat/:path*`,
      },
    ];
  },
};

export default nextConfig;
```

---

## `src/app/page.tsx`

```typescript
import Chat from './components/Chat';

export default function Home() {
  return (
    <main style={{
      minHeight: '100vh',
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      background: 'linear-gradient(135deg, #0f0c29, #302b63, #24243e)',
      padding: '20px'
    }}>
      <Chat />
    </main>
  );
}
```

---

## `src/app/components/Chat.tsx`

```typescript
'use client';

import { useState, useRef, FormEvent, useEffect } from 'react';

interface Message {
    role: 'user' | 'bot';
    content: string;
    file?: {
        name: string;
        size: number;
        type: string;
    };
}

export default function Chat() {
    const [messages, setMessages] = useState<Message[]>([
        {
            role: 'bot',
            content: `🎓 ¡Hola! Soy Alex, tu consejero vocacional.

Puedo ayudarte a encontrar la carrera ideal según tus intereses y notas. Tengo información sobre 41 carreras profesionales y los beneficios de admisión disponibles.

¿Qué te gustaría hacer?

• 📝 **Pregúntame por una carrera**: "Cuéntame sobre Ingeniería de Software"
• 📎 **Sube tu constancia de notas** (PDF) para evaluar tu perfil y ver si aplicas a algún beneficio
• 💬 **Conversemos** para descubrir qué carreras podrían gustarte
• 🎬 **Pide recomendaciones**: "Recomiéndame un influencer de Medicina"

¡Empieza cuando quieras!`
        }
    ]);
    const [inputValue, setInputValue] = useState('');
    const [selectedFile, setSelectedFile] = useState<File | null>(null);
    const [isStreaming, setIsStreaming] = useState(false);
    const [isUploading, setIsUploading] = useState(false);
    const [conversationId] = useState(() => crypto.randomUUID());
    const messagesEndRef = useRef<HTMLDivElement>(null);
    const abortControllerRef = useRef<AbortController | null>(null);
    const fileInputRef = useRef<HTMLInputElement>(null);

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    };

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    const formatFileSize = (bytes: number): string => {
        if (bytes === 0) return '0 Bytes';
        const k = 1024;
        const sizes = ['Bytes', 'KB', 'MB', 'GB'];
        const i = Math.floor(Math.log(bytes) / Math.log(k));
        return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    };

    const handleFileSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (file) {
            if (file.size > 10 * 1024 * 1024) {
                alert('El archivo no puede superar los 10MB');
                if (fileInputRef.current) {
                    fileInputRef.current.value = '';
                }
                return;
            }

            if (file.type !== 'application/pdf') {
                alert('Solo se admiten archivos PDF (constancia de notas)');
                if (fileInputRef.current) {
                    fileInputRef.current.value = '';
                }
                return;
            }

            setSelectedFile(file);
        }
    };

    const removeFile = () => {
        setSelectedFile(null);
        if (fileInputRef.current) {
            fileInputRef.current.value = '';
        }
    };

    const obtenerInfluencer = async (carreraNombre: string) => {
        try {
            const response = await fetch(`/api/chat/influencer/${encodeURIComponent(carreraNombre)}`);
            const texto = await response.text();

            setMessages(prev => [...prev, {
                role: 'bot',
                content: `📚 Contenido recomendado para ${carreraNombre}\n\n${texto}\n\n✨ ¿Te gustaría conocer más sobre otra carrera? Solo escríbeme su nombre.`
            }]);
        } catch {
            setMessages(prev => [...prev, {
                role: 'bot',
                content: `❌ Lo siento, no pude encontrar información para "${carreraNombre}". ¿Te gustaría probar con otra carrera?`
            }]);
        }
    };

    const sendMessage = async (e: FormEvent) => {
        e.preventDefault();
        if ((!inputValue.trim() && !selectedFile) || isStreaming || isUploading) return;

        const userMessage = inputValue.trim();
        const fileToSend = selectedFile;

        const palabrasClaveInfluencer = ['influencer', 'creador', 'youtube', 'recomienda', 'contenido'];
        const esComandoInfluencer = palabrasClaveInfluencer.some(p =>
            userMessage.toLowerCase().includes(p)
        );

        let carreraMencionada: string | null = null;
        if (esComandoInfluencer) {
            const palabras = userMessage.toLowerCase().split(/\s+/);
            for (let i = 0; i < palabras.length; i++) {
                if (palabrasClaveInfluencer.includes(palabras[i]) && i + 1 < palabras.length) {
                    const posibleCarrera = palabras[i + 1]
                        .replace(/[^a-záéíóúñ]/g, '');
                    if (posibleCarrera.length > 2) {
                        carreraMencionada = posibleCarrera.charAt(0).toUpperCase() + posibleCarrera.slice(1);
                        break;
                    }
                }
            }
        }

        if (esComandoInfluencer && carreraMencionada) {
            setInputValue('');
            setMessages(prev => [...prev, { role: 'user', content: userMessage }]);
            await obtenerInfluencer(carreraMencionada);
            return;
        }

        if (esComandoInfluencer && !carreraMencionada) {
            setInputValue('');
            setMessages(prev => [...prev, { role: 'user', content: userMessage }]);
            setMessages(prev => [...prev, {
                role: 'bot',
                content: '🎬 Claro, dime de qué carrera quieres conocer contenido recomendado. Por ejemplo: "influencer Medicina" o "recomiéndame un creador de Ingeniería de Software".'
            }]);
            return;
        }

        setInputValue('');

        if (userMessage && fileToSend) {
            setMessages(prev => [...prev, {
                role: 'user',
                content: `${userMessage}\n\n📎 Archivo adjunto: ${fileToSend.name} (${formatFileSize(fileToSend.size)})`,
                file: {
                    name: fileToSend.name,
                    size: fileToSend.size,
                    type: fileToSend.type
                }
            }]);
        } else if (userMessage) {
            setMessages(prev => [...prev, { role: 'user', content: userMessage }]);
        } else if (fileToSend) {
            setMessages(prev => [...prev, {
                role: 'user',
                content: `📎 Archivo adjunto: ${fileToSend.name} (${formatFileSize(fileToSend.size)})`,
                file: {
                    name: fileToSend.name,
                    size: fileToSend.size,
                    type: fileToSend.type
                }
            }]);
        }

        setIsStreaming(true);
        setIsUploading(!!fileToSend);

        setMessages(prev => [...prev, { role: 'bot', content: '' }]);

        abortControllerRef.current = new AbortController();

        try {
            let response;

            if (fileToSend) {
                const formData = new FormData();
                formData.append('mensaje', userMessage || 'Analiza esta constancia de notas');
                formData.append('conversationId', conversationId);
                formData.append('archivo', fileToSend);

                response = await fetch('/api/chat/stream-with-file', {
                    method: 'POST',
                    body: formData,
                    signal: abortControllerRef.current.signal
                });

                setSelectedFile(null);
                if (fileInputRef.current) {
                    fileInputRef.current.value = '';
                }
            } else {
                response = await fetch('/api/chat/stream', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        mensaje: userMessage,
                        conversationId: conversationId
                    }),
                    signal: abortControllerRef.current.signal
                });
            }

            if (!response.ok) {
                throw new Error(`Error ${response.status}: ${response.statusText}`);
            }

            setIsUploading(false);

            const reader = response.body?.getReader();
            const decoder = new TextDecoder();

            if (!reader) throw new Error('No se pudo leer el stream');

            let accumulatedText = '';
            let buffer = '';

            while (true) {
                const { done, value } = await reader.read();
                if (done) break;

                buffer += decoder.decode(value, { stream: true });
                const lines = buffer.split('\n');
                buffer = lines.pop() || '';

                for (const line of lines) {
                    if (line.startsWith('data:')) {
                        const chunk = line.slice(5).trim();
                        if (chunk && chunk !== '[DONE]') {
                            accumulatedText += chunk;
                            setMessages(prev => {
                                const newMessages = [...prev];
                                const lastMessage = newMessages[newMessages.length - 1];
                                if (lastMessage.role === 'bot') {
                                    lastMessage.content = accumulatedText;
                                }
                                return newMessages;
                            });
                        }
                    }
                }
            }

            if (buffer.startsWith('data:')) {
                const chunk = buffer.slice(5).trim();
                if (chunk && chunk !== '[DONE]') {
                    setMessages(prev => {
                        const newMessages = [...prev];
                        const lastMessage = newMessages[newMessages.length - 1];
                        if (lastMessage.role === 'bot') {
                            lastMessage.content += chunk;
                        }
                        return newMessages;
                    });
                }
            }

        } catch (error: any) {
            if (error.name !== 'AbortError') {
                console.error('Error en stream:', error);
                setIsUploading(false);
                setMessages(prev => {
                    const newMessages = [...prev];
                    const lastMessage = newMessages[newMessages.length - 1];
                    if (lastMessage.role === 'bot') {
                        if (error.message.includes('413')) {
                            lastMessage.content = '❌ El archivo es demasiado grande. Por favor, sube un archivo de menos de 10MB.';
                        } else {
                            lastMessage.content = '❌ Lo siento, hubo un error al procesar tu mensaje. ¿Podemos intentarlo de nuevo?\n\nTips:\n• Asegúrate de que el archivo sea una constancia de notas en PDF válida\n• Intenta escribir tus promedios directamente si el PDF no se lee correctamente';
                        }
                    }
                    return newMessages;
                });
            }
        } finally {
            setIsStreaming(false);
            abortControllerRef.current = null;
        }
    };

    return (
        <div style={{
            width: '100%',
            maxWidth: '900px',
            height: '90vh',
            background: 'rgba(255, 255, 255, 0.05)',
            backdropFilter: 'blur(20px)',
            borderRadius: '24px',
            border: '1px solid rgba(255, 255, 255, 0.1)',
            display: 'flex',
            flexDirection: 'column',
            overflow: 'hidden',
            boxShadow: '0 25px 50px rgba(0, 0, 0, 0.5)',
        }}>
            {/* Header */}
            <div style={{
                padding: '24px 28px',
                background: 'linear-gradient(135deg, rgba(99, 102, 241, 0.3), rgba(168, 85, 247, 0.3))',
                borderBottom: '1px solid rgba(255, 255, 255, 0.1)',
                display: 'flex',
                alignItems: 'center',
                gap: '16px',
            }}>
                <div style={{
                    width: '48px',
                    height: '48px',
                    borderRadius: '50%',
                    background: 'linear-gradient(135deg, #6366f1, #a855f7)',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    fontSize: '24px',
                    color: 'white',
                    flexShrink: 0,
                }}>
                    🎓
                </div>
                <div style={{ flex: 1 }}>
                    <h1 style={{ color: 'white', fontSize: '18px', fontWeight: 600, margin: 0 }}>
                        Consejero Vocacional
                    </h1>
                    <p style={{ color: 'rgba(255, 255, 255, 0.6)', fontSize: '13px', margin: '4px 0 0 0' }}>
                        Tu amigo Alex — Orientación profesional con IA
                    </p>
                </div>
                <div style={{
                    display: 'flex',
                    gap: '8px',
                    background: 'rgba(255,255,255,0.1)',
                    padding: '6px 12px',
                    borderRadius: '20px',
                }}>
                    <span style={{ fontSize: '12px', color: 'rgba(255,255,255,0.8)' }}>
                        📎 Sube tu constancia en PDF
                    </span>
                </div>
            </div>

            {/* Messages */}
            <div style={{
                flex: 1,
                overflowY: 'auto',
                padding: '24px 28px',
                display: 'flex',
                flexDirection: 'column',
                gap: '16px',
            }}>
                {messages.map((msg, idx) => (
                    <div
                        key={idx}
                        style={{
                            display: 'flex',
                            gap: '12px',
                            maxWidth: '85%',
                            alignSelf: msg.role === 'user' ? 'flex-end' : 'flex-start',
                            flexDirection: msg.role === 'user' ? 'row-reverse' : 'row',
                            animation: 'messageIn 0.3s ease-out',
                        }}
                    >
                        <div style={{
                            width: '36px',
                            height: '36px',
                            borderRadius: '50%',
                            flexShrink: 0,
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            fontSize: '16px',
                            background: msg.role === 'user'
                                ? 'linear-gradient(135deg, #6366f1, #7c3aed)'
                                : 'linear-gradient(135deg, #a855f7, #d946ef)',
                            color: 'white',
                        }}>
                            {msg.role === 'bot' ? '🎓' : '👤'}
                        </div>
                        <div style={{
                            padding: '14px 18px',
                            borderRadius: '18px',
                            fontSize: '15px',
                            lineHeight: '1.6',
                            color: 'rgba(255, 255, 255, 0.9)',
                            wordWrap: 'break-word',
                            whiteSpace: 'pre-wrap',
                            background: msg.role === 'user'
                                ? 'linear-gradient(135deg, #6366f1, #7c3aed)'
                                : 'rgba(255, 255, 255, 0.08)',
                            border: msg.role === 'bot' ? '1px solid rgba(255, 255, 255, 0.06)' : 'none',
                            borderBottomRightRadius: msg.role === 'user' ? '4px' : '18px',
                            borderBottomLeftRadius: msg.role === 'bot' ? '4px' : '18px',
                        }}>
                            {msg.content.split('\n').map((line, i) => (
                                <span key={i}>
                                    {line}
                                    {i < msg.content.split('\n').length - 1 && <br />}
                                </span>
                            ))}
                            {msg.role === 'bot' && isStreaming && idx === messages.length - 1 && msg.content === '' && (
                                <span className="cursor-blink">▊</span>
                            )}
                        </div>
                    </div>
                ))}

                {isUploading && (
                    <div style={{
                        alignSelf: 'flex-start',
                        padding: '16px 20px',
                        background: 'rgba(99, 102, 241, 0.2)',
                        borderRadius: '18px',
                        borderBottomLeftRadius: '4px',
                        display: 'flex',
                        alignItems: 'center',
                        gap: '12px',
                        border: '1px solid rgba(99, 102, 241, 0.3)',
                        animation: 'messageIn 0.3s ease-out',
                    }}>
                        <div style={{
                            width: '20px',
                            height: '20px',
                            border: '2px solid rgba(99, 102, 241, 0.3)',
                            borderTopColor: '#6366f1',
                            borderRadius: '50%',
                            animation: 'spin 1s linear infinite',
                        }} />
                        <span style={{ color: 'white', fontSize: '14px' }}>
                            Subiendo y analizando archivo...
                        </span>
                    </div>
                )}

                {isStreaming && !isUploading && (
                    <div style={{
                        alignSelf: 'flex-start',
                        padding: '16px 20px',
                        background: 'rgba(255, 255, 255, 0.08)',
                        borderRadius: '18px',
                        borderBottomLeftRadius: '4px',
                        display: 'flex',
                        gap: '4px',
                        border: '1px solid rgba(255, 255, 255, 0.06)',
                        animation: 'messageIn 0.3s ease-out',
                    }}>
                        <span style={{ width: '8px', height: '8px', borderRadius: '50%', background: 'rgba(255, 255, 255, 0.5)', animation: 'typing 1.4s infinite ease-in-out' }}></span>
                        <span style={{ width: '8px', height: '8px', borderRadius: '50%', background: 'rgba(255, 255, 255, 0.5)', animation: 'typing 1.4s infinite ease-in-out', animationDelay: '0.2s' }}></span>
                        <span style={{ width: '8px', height: '8px', borderRadius: '50%', background: 'rgba(255, 255, 255, 0.5)', animation: 'typing 1.4s infinite ease-in-out', animationDelay: '0.4s' }}></span>
                    </div>
                )}
                <div ref={messagesEndRef} />
            </div>

            {selectedFile && (
                <div style={{
                    padding: '10px 20px',
                    background: 'rgba(99, 102, 241, 0.2)',
                    borderTop: '1px solid rgba(255,255,255,0.1)',
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    margin: '0 20px',
                    borderRadius: '8px',
                }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                        <span style={{ fontSize: '20px' }}>📄</span>
                        <div>
                            <div style={{ fontSize: '14px', color: 'white', fontWeight: 500 }}>
                                {selectedFile.name}
                            </div>
                            <div style={{ fontSize: '12px', color: 'rgba(255,255,255,0.6)' }}>
                                {formatFileSize(selectedFile.size)}
                            </div>
                        </div>
                    </div>
                    <button
                        onClick={removeFile}
                        style={{
                            background: 'rgba(255,255,255,0.1)',
                            border: 'none',
                            color: 'white',
                            padding: '6px 12px',
                            borderRadius: '6px',
                            cursor: 'pointer',
                            fontSize: '12px',
                            transition: 'background 0.2s',
                        }}
                        onMouseEnter={(e) => { e.currentTarget.style.background = 'rgba(255,255,255,0.2)'; }}
                        onMouseLeave={(e) => { e.currentTarget.style.background = 'rgba(255,255,255,0.1)'; }}
                    >
                        Quitar
                    </button>
                </div>
            )}

            {/* Input */}
            <form onSubmit={sendMessage} style={{
                padding: '20px 28px',
                borderTop: '1px solid rgba(255, 255, 255, 0.1)',
                display: 'flex',
                gap: '12px',
                background: 'rgba(0, 0, 0, 0.2)',
            }}>
                <button
                    type="button"
                    onClick={() => fileInputRef.current?.click()}
                    disabled={isStreaming || isUploading}
                    style={{
                        padding: '14px',
                        background: 'rgba(255, 255, 255, 0.1)',
                        border: '1px solid rgba(255, 255, 255, 0.2)',
                        borderRadius: '14px',
                        color: 'white',
                        cursor: (isStreaming || isUploading) ? 'not-allowed' : 'pointer',
                        fontSize: '18px',
                        transition: 'all 0.2s',
                        opacity: (isStreaming || isUploading) ? 0.5 : 1,
                    }}
                    onMouseEnter={(e) => {
                        if (!isStreaming && !isUploading) {
                            e.currentTarget.style.background = 'rgba(255, 255, 255, 0.2)';
                            e.currentTarget.style.transform = 'scale(1.05)';
                        }
                    }}
                    onMouseLeave={(e) => {
                        e.currentTarget.style.background = 'rgba(255, 255, 255, 0.1)';
                        e.currentTarget.style.transform = 'scale(1)';
                    }}
                >
                    📎
                </button>

                <input
                    type="text"
                    value={inputValue}
                    onChange={(e) => setInputValue(e.target.value)}
                    placeholder={selectedFile ? "Escribe un comentario sobre el archivo..." : 'Escribe tu mensaje aquí... (ej: "Quiero estudiar Medicina")'}
                    disabled={isStreaming || isUploading}
                    autoFocus
                    style={{
                        flex: 1,
                        padding: '14px 20px',
                        border: '1px solid rgba(255, 255, 255, 0.1)',
                        borderRadius: '14px',
                        background: 'rgba(255, 255, 255, 0.06)',
                        color: 'white',
                        fontSize: '15px',
                        outline: 'none',
                        transition: 'border-color 0.2s',
                    }}
                />

                <button
                    type="submit"
                    disabled={isStreaming || isUploading || (!inputValue.trim() && !selectedFile)}
                    style={{
                        padding: '14px 24px',
                        border: 'none',
                        borderRadius: '14px',
                        background: 'linear-gradient(135deg, #6366f1, #7c3aed)',
                        color: 'white',
                        fontSize: '15px',
                        fontWeight: 600,
                        cursor: (isStreaming || isUploading || (!inputValue.trim() && !selectedFile)) ? 'not-allowed' : 'pointer',
                        transition: 'transform 0.15s, box-shadow 0.15s',
                        whiteSpace: 'nowrap',
                        opacity: (isStreaming || isUploading || (!inputValue.trim() && !selectedFile)) ? 0.5 : 1,
                    }}
                    onMouseEnter={(e) => {
                        if (!isStreaming && !isUploading && (inputValue.trim() || selectedFile)) {
                            e.currentTarget.style.transform = 'translateY(-1px)';
                            e.currentTarget.style.boxShadow = '0 8px 20px rgba(99, 102, 241, 0.4)';
                        }
                    }}
                    onMouseLeave={(e) => {
                        e.currentTarget.style.transform = 'translateY(0)';
                        e.currentTarget.style.boxShadow = 'none';
                    }}
                >
                    {isUploading ? 'Subiendo...' : 'Enviar'}
                </button>
            </form>

            <input
                ref={fileInputRef}
                type="file"
                onChange={handleFileSelect}
                accept=".pdf"
                style={{ display: 'none' }}
            />

            <style>{`
                @keyframes messageIn {
                    from { opacity: 0; transform: translateY(10px) scale(0.98); }
                    to { opacity: 1; transform: translateY(0) scale(1); }
                }
                @keyframes typing {
                    0%, 60%, 100% { transform: translateY(0); opacity: 0.4; }
                    30% { transform: translateY(-8px); opacity: 1; }
                }
                @keyframes spin {
                    to { transform: rotate(360deg); }
                }
                @keyframes blink {
                    0%, 100% { opacity: 1; }
                    50% { opacity: 0; }
                }
                .cursor-blink {
                    animation: blink 1s infinite;
                    display: inline-block;
                    margin-left: 2px;
                }
                div[style*="overflowY: auto"]::-webkit-scrollbar { width: 6px; }
                div[style*="overflowY: auto"]::-webkit-scrollbar-track { background: transparent; }
                div[style*="overflowY: auto"]::-webkit-scrollbar-thumb { background: rgba(255, 255, 255, 0.2); border-radius: 3px; }
            `}</style>
        </div>
    );
}
```

---

## Cambios realizados respecto al código original

| Aspecto | Original | Modificado |
|---------|----------|------------|
| **Tipos de archivo** | PDF, TXT, DOC, DOCX, JPG, PNG | Solo **PDF** (el backend solo analiza constancias de notas) |
| **Límite de tamaño** | 5 MB | **10 MB** (alineado con `spring.servlet.multipart.max-file-size`) |
| **`accept` del input file** | `.pdf,.txt,.doc,.docx,.jpg,.jpeg,.png` | **`.pdf`** |
| **Mensaje de bienvenida** | Lista hardcodeada de 41 carreras | Mensaje genérico explicando funcionalidades |
| **`carrerasConocidas` hardcodeadas** | Array con 14 carreras para detectar comandos "influencer" | Eliminado — extracción genérica de la palabra después del comando |
| **Lista hardcodeada de influencers** | 6 carreras con nombres fijos | Eliminado — el backend devuelve la info a través de `GET /api/chat/influencer/{nombre}` |
| **`isInfluencerSuggestion`** | Propiedad en `Message` con estilo diferenciado | Eliminado — los mensajes de influencer usan el mismo estilo que otros mensajes del bot |
| **Manejo de errores** | Errores 415 para tipo no soportado | Simplificado — solo PDF, error 413 para tamaño |

## Endpoints del Backend (ya implementados)

| Método | Ruta | Propósito |
|--------|------|-----------|
| `POST` | `/api/chat/stream` | Chat conversacional con streaming SSE |
| `POST` | `/api/chat/stream-with-file` | Subir PDF de constancia + análisis |
| `GET` | `/api/chat/influencer/{nombre}` | Obtener creador de contenido de una carrera |

## Proxy (Next.js)

El `next.config.ts` redirige `/api/chat/*` a `http://localhost:8080/api/chat/*` mediante rewrites.

## Variables de Entorno

```env
# .env.local (Next.js)
API_URL=http://localhost:8080
```
