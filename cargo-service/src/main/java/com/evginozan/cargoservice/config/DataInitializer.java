package com.evginozan.cargoservice.config;

import com.evginozan.cargoservice.model.Branch;
import com.evginozan.cargoservice.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final BranchRepository branchRepository;

    @Override
    public void run(String... args) throws Exception {
        initBranches();
    }

    private void initBranches() {
        if (branchRepository.count() == 0) {
            // Ankara Şubeleri
            Branch ankaraKecioren = Branch.builder()
                    .name("Ankara Keçiören Şubesi")
                    .city("Ankara")
                    .district("Keçiören")
                    .address("Keçiören Caddesi No: 123")
                    .phone("03124567890")
                    .isTransferCenter(false)
                    .build();
            branchRepository.save(ankaraKecioren);

            Branch ankaraUmitkoy = Branch.builder()
                    .name("Ankara Ümitköy Şubesi")
                    .city("Ankara")
                    .district("Ümitköy")
                    .address("Ümitköy Caddesi No: 45")
                    .phone("03124561234")
                    .isTransferCenter(false)
                    .build();
            branchRepository.save(ankaraUmitkoy);

            Branch ankaraSincan = Branch.builder()
                    .name("Ankara Sincan Şubesi")
                    .city("Ankara")
                    .district("Sincan")
                    .address("Sincan Merkez Caddesi No: 78")
                    .phone("03123459876")
                    .isTransferCenter(false)
                    .build();
            branchRepository.save(ankaraSincan);

            // İstanbul Şubeleri
            Branch istanbulKadikoy = Branch.builder()
                    .name("İstanbul Kadıköy Şubesi")
                    .city("İstanbul")
                    .district("Kadıköy")
                    .address("Kadıköy Bağdat Caddesi No: 25")
                    .phone("02163334455")
                    .isTransferCenter(false)
                    .build();
            branchRepository.save(istanbulKadikoy);

            Branch istanbulBesiktas = Branch.builder()
                    .name("İstanbul Beşiktaş Şubesi")
                    .city("İstanbul")
                    .district("Beşiktaş")
                    .address("Beşiktaş Merkez Mahallesi No: 12")
                    .phone("02125556677")
                    .isTransferCenter(false)
                    .build();
            branchRepository.save(istanbulBesiktas);

            // İzmir Şubeleri
            Branch izmirKarsiyaka = Branch.builder()
                    .name("İzmir Karşıyaka Şubesi")
                    .city("İzmir")
                    .district("Karşıyaka")
                    .address("Karşıyaka Sahil Caddesi No: 34")
                    .phone("02323334455")
                    .isTransferCenter(false)
                    .build();
            branchRepository.save(izmirKarsiyaka);

            Branch izmirBornova = Branch.builder()
                    .name("İzmir Bornova Şubesi")
                    .city("İzmir")
                    .district("Bornova")
                    .address("Bornova Üniversite Caddesi No: 67")
                    .phone("02323456789")
                    .isTransferCenter(false)
                    .build();
            branchRepository.save(izmirBornova);

            // Transfer Merkezleri
            Branch ankaraTransfer = Branch.builder()
                    .name("Ankara Transfer Merkezi")
                    .city("Ankara")
                    .district("Kazan")
                    .address("Kazan Lojistik Merkezi No: 1")
                    .phone("03129998877")
                    .isTransferCenter(true)
                    .build();
            branchRepository.save(ankaraTransfer);

            Branch istanbulTransfer = Branch.builder()
                    .name("İstanbul Transfer Merkezi")
                    .city("İstanbul")
                    .district("Tuzla")
                    .address("Tuzla Lojistik Bölgesi No: 10")
                    .phone("02169998877")
                    .isTransferCenter(true)
                    .build();
            branchRepository.save(istanbulTransfer);

            Branch izmirTransfer = Branch.builder()
                    .name("İzmir Transfer Merkezi")
                    .city("İzmir")
                    .district("Çiğli")
                    .address("Çiğli Organize Sanayi Bölgesi No: 5")
                    .phone("02329998877")
                    .isTransferCenter(true)
                    .build();
            branchRepository.save(izmirTransfer);
        }
    }
}