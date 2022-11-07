#include <stddef.h>
#include <html.h>

#include "uk_co_therhys_MD_Converter.h"

const char* md2html(const char* in){
    hoedown_renderer* renderer;
    hoedown_document* document;
    hoedown_buffer* buffer;

    scidown_render_flags html_mode = SCIDOWN_RENDER_MERMAID | SCIDOWN_RENDER_CHARTER;

    localization_t local = {
            (char*) "Figure",
            (char*) "Listing",
            (char*) "Table"
    };

    renderer = hoedown_html_renderer_new(html_mode, 0, local);

    ext_definition def = {
            (char*) "",
            (char*) ""
    };

    document = hoedown_document_new(renderer,
                                    (hoedown_extensions) (HOEDOWN_EXT_BLOCK | HOEDOWN_EXT_SPAN | HOEDOWN_EXT_FLAGS),
                                    &def,
                                    ".",
                                    16);

    buffer = hoedown_buffer_new(4096);

    hoedown_document_render(document, buffer, (uint8_t*) in, strlen(in), 0);

    const char* buf_cstr = strdup(hoedown_buffer_cstr(buffer));

    hoedown_html_renderer_free(renderer);
    hoedown_document_free(document);
    hoedown_buffer_free(buffer);

    return buf_cstr;
}

JNIEXPORT jstring JNICALL Java_uk_co_therhys_MD_Converter_md2html
  (JNIEnv * env, jobject this, jstring markdown) {

    const char* cmarkdown = (*env)->GetStringUTFChars(env, markdown, NULL);

    const char* chtml;

    if(strlen(cmarkdown) != 0){
        chtml = md2html(cmarkdown);
    }else{
        chtml = "";
    }

    jstring html = (*env)->NewStringUTF(env, chtml);

    return html;
}

