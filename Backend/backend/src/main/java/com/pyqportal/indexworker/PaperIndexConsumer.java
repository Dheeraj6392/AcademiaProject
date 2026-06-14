package com.pyqportal.indexworker;

import com.pyqportal.search.PaperDocument;
import com.pyqportal.search.PaperSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaperIndexConsumer {

    private final PaperSearchRepository paperSearchRepository;
    private final RestTemplate restTemplate;

    /**
     * Manual ack — only acknowledged after successful indexing.
     * On any exception Kafka redelivers the message (do NOT ack on failure).
     */
    @KafkaListener(topics = "paper.uploaded", groupId = "index-worker-group")
    public void consume(ConsumerRecord<String, PaperUploadedEvent> record, Acknowledgment ack) {
        PaperUploadedEvent event = record.value();
        log.info("Received PaperUploadedEvent paperId={}", event.paperId());

        try {
            // 1. Download PDF bytes from Cloudinary
            byte[] pdfBytes = restTemplate.getForObject(event.fileUrl(), byte[].class);

            // 2. Extract text with PDFBox 3.x (use Loader.loadPDF, not PDDocument.load)
            String content = "";
            if (pdfBytes != null && pdfBytes.length > 0) {
                try (PDDocument doc = Loader.loadPDF(pdfBytes)) {
                    content = new PDFTextStripper().getText(doc);
                }
            }

            // 3. Build and save Elasticsearch document
            PaperDocument doc = PaperDocument.builder()
                    .id(event.paperId())
                    .title(event.title())
                    .subject(event.subject())
                    .branch(event.branch())
                    .year(event.year())
                    .examType(event.examType())
                    .content(content)
                    .build();

            paperSearchRepository.save(doc);
            log.info("Indexed paper {} in Elasticsearch ({} chars)", event.paperId(), content.length());

            // 4. Acknowledge only after successful save
            ack.acknowledge();

        } catch (Exception e) {
            log.error("Failed to index paper {} — Kafka will redeliver", event.paperId(), e);
            // Intentionally NOT calling ack.acknowledge()
        }
    }
}
